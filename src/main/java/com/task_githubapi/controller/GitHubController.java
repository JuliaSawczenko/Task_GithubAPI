package com.task_githubapi.controller;

import com.task_githubapi.dto.RepositoryDTO;
import com.task_githubapi.exception.GitHubApiException;
import com.task_githubapi.exception.UserNotFoundException;
import com.task_githubapi.service.GitHubRepoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/github")
public class GitHubController {

    private final GitHubRepoService gitHubRepoService;

    public GitHubController(GitHubRepoService gitHubRepoService) {
        this.gitHubRepoService = gitHubRepoService;
    }

    @GetMapping(value = "/user/{username}/repositories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RepositoryDTO>> getRepositoriesByUsername (final @PathVariable String username) throws UserNotFoundException, GitHubApiException {
        List<RepositoryDTO> repositoryDTOs = gitHubRepoService.assembleRepositoriesWithBranches(username);
        return ResponseEntity.ok(repositoryDTOs);
    }
}
