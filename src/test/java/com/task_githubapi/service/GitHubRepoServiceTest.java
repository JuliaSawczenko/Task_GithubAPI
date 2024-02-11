package com.task_githubapi.service;

import com.task_githubapi.dto.RepositoryDTO;
import com.task_githubapi.exception.GitHubApiException;
import com.task_githubapi.exception.UserNotFoundException;
import com.task_githubapi.mocks.BranchModelMock;
import com.task_githubapi.mocks.RepositoryModelMock;
import com.task_githubapi.model.BranchModel;
import com.task_githubapi.model.RepositoryModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static com.task_githubapi.mocks.MockedValues.REPO_NAME;
import static com.task_githubapi.mocks.MockedValues.USERNAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GitHubRepoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GitHubUserService gitHubUserService;

    @Mock
    private GitHubBranchService gitHubBranchService;

    @InjectMocks
    private GitHubRepoService gitHubRepoService;


    @Test
    void whenFetchUserRepositories_thenReturnNonForkRepositories() throws UserNotFoundException, GitHubApiException {
        // Given
        RepositoryModel forkedRepoModel = RepositoryModelMock.getRepositoryModel(true);
        RepositoryModel nonForkedRepoModel = RepositoryModelMock.getRepositoryModel(false);
        RepositoryModel[] repositoryModels = {forkedRepoModel, nonForkedRepoModel};

        when(restTemplate.getForEntity(anyString(), eq(RepositoryModel[].class)))
                .thenReturn(new ResponseEntity<>(repositoryModels, HttpStatus.OK));

        // When
        List<RepositoryModel> result = gitHubRepoService.fetchUserRepositories(USERNAME);

        // Then
        assertEquals(1, result.size(), "Should only return one non-forked repository");
        assertFalse(result.get(0).isFork(), "The returned repository should not be a fork");
        verify(gitHubUserService).getUserByUsername(USERNAME);
        verify(restTemplate).getForEntity(eq(gitHubRepoService.buildReposUrl(USERNAME)), eq(RepositoryModel[].class));

    }

    @Test
    void whenFetchUserRepositories_givenNonExistentUser_thenThrowUserNotFoundException() throws UserNotFoundException, GitHubApiException {
        doThrow(UserNotFoundException.class).when(gitHubUserService).getUserByUsername(anyString());

        assertThrows(UserNotFoundException.class, () -> gitHubRepoService.fetchUserRepositories(USERNAME));
    }

    @Test
    void whenFetchUserRepositories_givenUserWithNoRepositories_thenReturnEmptyList() throws UserNotFoundException, GitHubApiException {
        when(restTemplate.getForEntity(anyString(), eq(RepositoryModel[].class)))
                .thenReturn(new ResponseEntity<>(new RepositoryModel[0], HttpStatus.OK));

        List<RepositoryModel> result = gitHubRepoService.fetchUserRepositories(USERNAME);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenAssembleRepositoriesWithBranches_thenReturnRepositoriesWithBranchesDTO() throws UserNotFoundException, GitHubApiException {
        // Given
        RepositoryModel repoModel = RepositoryModelMock.getRepositoryModel(false);
        BranchModel branchModel = BranchModelMock.getBranchModel();

        when(gitHubBranchService.fetchRepositoryBranches(USERNAME, REPO_NAME)).thenReturn(Collections.singletonList(branchModel));
        when(restTemplate.getForEntity(anyString(), eq(RepositoryModel[].class)))
                .thenReturn(new ResponseEntity<>(new RepositoryModel[]{repoModel}, HttpStatus.OK));

        // When
        List<RepositoryDTO> result = gitHubRepoService.assembleRepositoriesWithBranches(USERNAME);

        // Then
        assertEquals(1, result.size());
        verify(gitHubBranchService).fetchRepositoryBranches(USERNAME, REPO_NAME);
        assertNotNull(result.get(0));
    }

    @Test
    void whenAssembleRepositoriesWithBranches_givenRepoWithNullBranches_thenReturnRepoWithEmptyBranchList() throws UserNotFoundException, GitHubApiException {
        RepositoryModel repoModel = RepositoryModelMock.getRepositoryModel(false);
        when(restTemplate.getForEntity(anyString(), eq(RepositoryModel[].class)))
                .thenReturn(new ResponseEntity<>(new RepositoryModel[]{repoModel}, HttpStatus.OK));
        when(gitHubBranchService.fetchRepositoryBranches(USERNAME, REPO_NAME)).thenReturn(null);

        List<RepositoryDTO> result = gitHubRepoService.assembleRepositoriesWithBranches(USERNAME);

        assertNotNull(result.get(0).getBranches(), "Branches list should not be null");
        assertTrue(result.get(0).getBranches().isEmpty(), "Branches list should be empty if null branches are fetched");
    }



}
