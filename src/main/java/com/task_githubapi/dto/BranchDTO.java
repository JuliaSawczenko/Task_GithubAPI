package com.task_githubapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String lastCommit;
}
