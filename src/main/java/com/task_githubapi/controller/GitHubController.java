package com.task_githubapi.controller;

import com.task_githubapi.dto.RepositoryDTO;
import com.task_githubapi.exception.GitHubApiException;
import com.task_githubapi.exception.UserNotFoundException;
import com.task_githubapi.service.GitHubService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class GitHubController {

    private final GitHubService gitHubRepoService;

    public GitHubController(GitHubService gitHubRepoService) {
        this.gitHubRepoService = gitHubRepoService;
    }

    @GetMapping(value = "/users/{username}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepositoryDTO> getRepositoriesByUsername (final @PathVariable String username) throws UserNotFoundException, GitHubApiException, ExecutionException, InterruptedException {
        return gitHubRepoService.getRepositoriesWithBranches(username);
    }
}
