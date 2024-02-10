package com.task_githubapi.service;

import com.task_githubapi.dto.BranchDTO;
import com.task_githubapi.dto.RepositoryDTO;
import com.task_githubapi.exception.GitHubApiException;
import com.task_githubapi.exception.UserNotFoundException;
import com.task_githubapi.mapper.BranchMapper;
import com.task_githubapi.mapper.RepositoryMapper;
import com.task_githubapi.model.BranchModel;
import com.task_githubapi.model.RepositoryModel;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GitHubRepoService {

    private final RestTemplate restTemplate;
    private final GitHubUserService gitHubUserService;
    private final GitHubBranchService gitHubBranchService;
    private final BranchMapper branchMapper = Mappers.getMapper(BranchMapper.class);
    private final RepositoryMapper repositoryMapper = Mappers.getMapper(RepositoryMapper.class);
    private static final Logger logger = LoggerFactory.getLogger(GitHubRepoService.class);


    public GitHubRepoService(RestTemplate restTemplate, GitHubUserService gitHubUserService, GitHubBranchService gitHubBranchService) {
        this.restTemplate = restTemplate;
        this.gitHubUserService = gitHubUserService;
        this.gitHubBranchService = gitHubBranchService;
    }

    public List<RepositoryModel> fetchUserRepositories(final String username) throws UserNotFoundException, GitHubApiException {
        logger.info("Fetching repositories for user: {}", username);
        gitHubUserService.getUserByUsername(username);
        String url = buildReposUrl(username);
        ResponseEntity<RepositoryModel[]> response = restTemplate.getForEntity(url, RepositoryModel[].class);

        List<RepositoryModel> repositories = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .filter(repo -> !repo.isFork())
                .collect(Collectors.toList());
        logger.info("Found {} repositories for user: {}", repositories.size(), username);
        return repositories;
    }

    public List<RepositoryDTO> assembleRepositoriesWithBranches(final String username) throws UserNotFoundException, GitHubApiException {
        logger.info("Assembling repositories with branches for user: {}", username);
        List<RepositoryModel> repos = fetchUserRepositories(username);

        return repos.stream()
                .map(repo -> {
                    List<BranchModel> branchModels = gitHubBranchService.fetchRepositoryBranches(username, repo.getName());
                    List<BranchDTO> branchDTOs = branchModels.stream()
                            .map(branchMapper::branchModelToBranchDTO)
                            .toList();
                    return repositoryMapper.repositoryModelToRepositoryDTO(repo, branchDTOs);
                })
                .collect(Collectors.toList());
    }

    private String buildReposUrl(final String username) {
        return "https://api.github.com/users/" + username + "/repos";
    }


}
