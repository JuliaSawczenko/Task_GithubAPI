package com.task_githubapi.service;

import com.task_githubapi.mocks.BranchModelMock;
import com.task_githubapi.model.BranchModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.task_githubapi.mocks.MockedValues.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubBranchServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GitHubBranchService gitHubBranchService;

    @Test
    void fetchRepositoryBranches_Success() {
        BranchModel[] branchModels = {BranchModelMock.getBranchModel(), BranchModelMock.getBranchModel()};
        when(restTemplate.getForEntity(anyString(), eq(BranchModel[].class)))
                .thenReturn(new ResponseEntity<>(branchModels, HttpStatus.OK));

        List<BranchModel> result = gitHubBranchService.fetchRepositoryBranches(USERNAME, REPO_NAME);

        assertEquals(2, result.size());
        assertEquals(BRANCH_NAME, result.get(0).getName());
        assertEquals(BRANCH_NAME, result.get(1).getName());
    }

    @Test
    void fetchRepositoryBranches_NoBranches() {
        when(restTemplate.getForEntity(anyString(), eq(BranchModel[].class)))
                .thenReturn(new ResponseEntity<>(new BranchModel[0], HttpStatus.OK));

        List<BranchModel> result = gitHubBranchService.fetchRepositoryBranches(USERNAME, REPO_NAME);

        assertTrue(result.isEmpty());
    }

}
