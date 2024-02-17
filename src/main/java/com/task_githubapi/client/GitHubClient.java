package com.task_githubapi.client;

import com.task_githubapi.exception.GitHubApiException;
import com.task_githubapi.exception.UserNotFoundException;
import com.task_githubapi.model.BranchModel;
import com.task_githubapi.model.RepositoryModel;
import jakarta.annotation.PreDestroy;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class GitHubClient {

    private static final String gitHubToken = System.getenv("GITHUB_JWT");

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    private final RestClient restClient;


    public GitHubClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Authorization", "Bearer " + gitHubToken)
                .defaultHeader("Accept", "application/vnd.github+json")
                .build();
    }


    public List<RepositoryModel> getUserRepositories(final String username) throws UserNotFoundException, ExecutionException, InterruptedException {
        Callable<List<RepositoryModel>> task = () -> {

            String userReposUrl = "https://api.github.com/users/{username}/repos";

            ResponseEntity<RepositoryModel[]> response = restClient.get()
                    .uri(userReposUrl, username)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, httpResponse) -> {
                        if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                            throw new UserNotFoundException(username);
                        }
                        throw new GitHubApiException("Error while fetching repositories: " + httpResponse.getStatusCode());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, httpResponse) -> {
                        throw new GitHubApiException("Server error: " + httpResponse.getStatusCode());
                    })
                    .toEntity(RepositoryModel[].class);


            RepositoryModel[] repositories = response.getBody();

            if (repositories == null) {
                return Collections.emptyList();
            }

            return Arrays.stream(repositories)
                    .filter(repo -> !repo.isFork())
                    .collect(Collectors.toList());
        };

            return executor.submit(task).get();

    }


    public List<BranchModel> getRepositoryBranches(final String username, String repoName) throws GitHubApiException, ExecutionException, InterruptedException {
        Callable<List<BranchModel>> task = () -> {

            String branchesUrl = String.format("/repos/%s/%s/branches", username, repoName);

            ResponseEntity<BranchModel[]> response = restClient.get()
                    .uri(branchesUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, httpResponse) -> {
                        throw new GitHubApiException("Error while fetching branches: " + httpResponse.getStatusCode());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, httpResponse) -> {
                        throw new GitHubApiException("Server error: " + httpResponse.getStatusCode());
                    })
                    .toEntity(BranchModel[].class);


            BranchModel[] branches = response.getBody();

            if (branches == null) {
                return Collections.emptyList();
            }

            return Arrays.asList(branches);
        };

        return executor.submit(task).get();

    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }


}