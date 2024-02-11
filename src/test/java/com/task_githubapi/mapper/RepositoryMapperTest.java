package com.task_githubapi.mapper;

import com.task_githubapi.dto.BranchDTO;
import com.task_githubapi.dto.RepositoryDTO;
import com.task_githubapi.mocks.BranchDTOMock;
import com.task_githubapi.mocks.RepositoryModelMock;
import com.task_githubapi.model.RepositoryModel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepositoryMapperTest {

    private final RepositoryMapper mapper = Mappers.getMapper(RepositoryMapper.class);

    @Test
    public void shouldCorrectlyMapRepositoryModelToRepositoryDTO() {
        // Given
        RepositoryModel repositoryModel = RepositoryModelMock.getRepositoryModel(false);
        List<BranchDTO> branchDTOs = new ArrayList<>();
        branchDTOs.add(BranchDTOMock.getBranchDTO());

        // When
        RepositoryDTO repositoryDTO = mapper.repositoryModelToRepositoryDTO(repositoryModel, branchDTOs);

        // Then
        assertEquals(repositoryDTO.getName(), repositoryModel.getName());
        assertEquals(repositoryDTO.getOwnerLogin(), repositoryModel.getOwner().getLogin());

        assertEquals(branchDTOs.size(), repositoryModel.getBranches().size());
        for (int i = 0; i < branchDTOs.size(); i++) {
            assertEquals(branchDTOs.get(i).getName(), repositoryModel.getBranches().get(i).getName());
            assertEquals(branchDTOs.get(i).getLastCommit(), repositoryModel.getBranches().get(i).getCommit().getSha());
        }
    }
}
