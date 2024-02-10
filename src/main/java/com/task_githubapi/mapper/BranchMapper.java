package com.task_githubapi.mapper;

import com.task_githubapi.dto.BranchDTO;
import com.task_githubapi.model.BranchModel;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    @Mapping(source = "branchModel.commit.sha", target = "lastCommit")
    BranchDTO branchModelToBranchDTO(BranchModel branchModel);
}
