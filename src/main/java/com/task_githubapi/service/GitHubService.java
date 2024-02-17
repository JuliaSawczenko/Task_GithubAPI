package com.task_githubapi.service;

import com.task_githubapi.client.GitHubClient;
import com.task_githubapi.dto.BranchDTO;
import com.task_githubapi.dto.RepositoryDTO;
import com.task_githubapi.exception.GitHubApiException;
import com.task_githubapi.exception.UserNotFoundException;
import com.task_githubapi.mapper.BranchMapper;
import com.task_githubapi.mapper.RepositoryMapper;
import com.task_githubapi.model.BranchModel;
import com.task_githubapi.model.RepositoryModel;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private final GitHubClient gitHubClient;
    private final BranchMapper branchMapper = Mappers.getMapper(BranchMapper.class);
    private final RepositoryMapper repositoryMapper = Mappers.getMapper(RepositoryMapper.class);

    public GitHubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<RepositoryDTO> getRepositoriesWithBranches(final String username) throws UserNotFoundException, GitHubApiException, ExecutionException, InterruptedException {
        List<RepositoryModel> repos = gitHubClient.getUserRepositories(username);
        List<RepositoryDTO> repositoryDTOs = new ArrayList<>();

        for (RepositoryModel repo : repos) {
            List<BranchModel> branchModels = gitHubClient.getRepositoryBranches(username, repo.getName());
            List<BranchDTO> branchDTOs = branchModels.stream()
                    .map(branchMapper::branchModelToBranchDTO)
                    .collect(Collectors.toList());

            repositoryDTOs.add(repositoryMapper.repositoryModelToRepositoryDTO(repo, branchDTOs));
        }

        return repositoryDTOs;
    }
}
