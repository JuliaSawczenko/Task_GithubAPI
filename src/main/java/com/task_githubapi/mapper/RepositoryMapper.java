package com.task_githubapi.mapper;

import com.task_githubapi.dto.BranchDTO;
import com.task_githubapi.dto.RepositoryDTO;
import com.task_githubapi.model.RepositoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {

    @Mapping(source = "repositoryModel.owner.login", target = "ownerLogin")
    @Mapping(target = "branches", source = "branchDTOs")
    RepositoryDTO repositoryModelToRepositoryDTO(RepositoryModel repositoryModel, List<BranchDTO> branchDTOs);

}
