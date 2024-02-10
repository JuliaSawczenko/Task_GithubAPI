package com.task_githubapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Data
public class RepositoryModel {

    @NotBlank
    private String name;

    @NotNull
    private Owner owner;
    private boolean isFork;
    private List<BranchModel> branches;

    @Data
    public static class Owner {
        private String login;
    }
}
