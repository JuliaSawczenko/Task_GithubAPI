package com.task_githubapi.mapper;

import com.task_githubapi.dto.BranchDTO;
import com.task_githubapi.mocks.BranchModelMock;
import com.task_githubapi.model.BranchModel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BranchMapperTest {

    private final BranchMapper mapper = Mappers.getMapper(BranchMapper.class);

    @Test
    public void shouldCorrectlyMapBranchModelToBranchDTO() {
        // Given
        BranchModel branchModel = BranchModelMock.getBranchModel();

        // When
        BranchDTO branchDTO = mapper.branchModelToBranchDTO(branchModel);

        // Then
        assertEquals(branchModel.getName(), branchDTO.getName());
        assertEquals(branchModel.getCommit().getSha(), branchDTO.getLastCommit());
    }
}
