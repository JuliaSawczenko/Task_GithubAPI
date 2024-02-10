package com.task_githubapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BranchDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String lastCommit;
}
