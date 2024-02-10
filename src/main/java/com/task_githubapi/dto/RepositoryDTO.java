package com.task_githubapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RepositoryDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String ownerLogin;

    private List<BranchDTO> branches;
}
