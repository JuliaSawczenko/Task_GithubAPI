package com.task_githubapi.dto;

import jakarta.validation.constraints.NotBlank;


public record BranchDTO(@NotBlank String name,
                        @NotBlank String lastCommit) {
}