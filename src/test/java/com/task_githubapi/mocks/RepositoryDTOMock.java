package com.task_githubapi.mocks;

import com.task_githubapi.dto.RepositoryDTO;

import java.util.List;

import static com.task_githubapi.mocks.MockedValues.OWNER_NAME;
import static com.task_githubapi.mocks.MockedValues.REPO_NAME;

public class RepositoryDTOMock {

    public RepositoryDTOMock() {
    }

    public static RepositoryDTO getRepositoryDTO() {
        return new RepositoryDTO(REPO_NAME, OWNER_NAME, List.of(BranchDTOMock.getBranchDTO()));
    }
}
