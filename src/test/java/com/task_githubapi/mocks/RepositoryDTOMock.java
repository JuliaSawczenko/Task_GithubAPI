package com.task_githubapi.mocks;

import com.task_githubapi.dto.RepositoryDTO;

import java.util.ArrayList;
import java.util.List;

import static com.task_githubapi.mocks.MockedValues.OWNER_NAME;
import static com.task_githubapi.mocks.MockedValues.REPO_NAME;

public class RepositoryDTOMock {

    public RepositoryDTOMock() {
    }

    public static List<RepositoryDTO> getRepositoryDTO() {
        List<RepositoryDTO> mockRepositoryDTOs = new ArrayList<>();
        mockRepositoryDTOs.add(new RepositoryDTO(REPO_NAME, OWNER_NAME, List.of(BranchDTOMock.getBranchDTO())));
        return mockRepositoryDTOs;
    }
}
