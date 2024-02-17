package com.task_githubapi.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;


public record RepositoryDTO(@NotBlank String name,
                            @NotBlank String ownerLogin,
                            List<BranchDTO> branches) {

}
