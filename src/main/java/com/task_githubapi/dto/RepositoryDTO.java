package com.task_githubapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RepositoryDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String ownerLogin;

    private List<BranchDTO> branches;
}
