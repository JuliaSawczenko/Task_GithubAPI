package com.task_githubapi.service;
import com.task_githubapi.model.BranchModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Service
public class GitHubBranchService {

    private final RestTemplate restTemplate;

    public GitHubBranchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BranchModel> fetchRepositoryBranches(final String username, String repoName)  {
        String url = buildBranchesUrl(username, repoName);
        ResponseEntity<BranchModel[]> response = restTemplate.getForEntity(url, BranchModel[].class);

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }

    private String buildBranchesUrl(final String username, String repoName) {
        return String.format("https://api.github.com/repos/%s/%s/branches", username, repoName);
    }
}
