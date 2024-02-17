package com.task_githubapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public class RepositoryModel {

    @NotBlank
    private String name;

    @NotNull
    private Owner owner;
    private boolean isFork;
    private List<BranchModel> branches;

    public String getName() {
        return name;
    }

    public Owner getOwner() {
        return owner;
    }

    public boolean isFork() {
        return isFork;
    }

    public static class Owner {
        private String login;

        public String getLogin() {
            return login;
        }
    }



}
