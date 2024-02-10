package com.task_githubapi.service;

import com.task_githubapi.model.BranchModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class GitHubBranchService {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(GitHubBranchService.class);


    @Value("${GITHUB_TOKEN}")
    private String gitHubToken;

    public GitHubBranchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BranchModel> fetchRepositoryBranches(final String username, String repoName) {
        String url = buildBranchesUrl(username, repoName);
        logger.info("Fetching branches for repository: {}/{}", username, repoName);
        ResponseEntity<BranchModel[]> response = restTemplate.getForEntity(url, BranchModel[].class);

        List<BranchModel> branches = Arrays.asList(Objects.requireNonNull(response.getBody()));
        logger.info("Found {} branches for repository: {}/{}", branches.size(), username, repoName);
        return branches;    }

    private String buildBranchesUrl(final String username, String repoName) {
        return String.format("https://api.github.com/repos/%s/%s/branches", username, repoName);
    }
}
