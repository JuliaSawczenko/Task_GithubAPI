package com.task_githubapi.mapper;

import com.task_githubapi.dto.BranchDTO;
import com.task_githubapi.dto.RepositoryDTO;
import com.task_githubapi.model.BranchModel;
import com.task_githubapi.model.RepositoryModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {

    @Mapping(target = "branches", ignore = true)
    @Mapping(source = "repositoryModel.owner.login", target = "ownerLogin")
    RepositoryDTO repositoryModelToRepositoryDTO(RepositoryModel repositoryModel, List<BranchDTO> branchDTOs);

    default String map(RepositoryModel.Owner owner) {
        return owner != null ? owner.getLogin() : null;
    }

    @AfterMapping
    default void setBranches(RepositoryModel repositoryModel, @MappingTarget RepositoryDTO repositoryDTO, List<BranchDTO> branchDTOs) {
        repositoryDTO.setBranches(branchDTOs);
    }
}
