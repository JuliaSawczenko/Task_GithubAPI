package com.task_githubapi.model;

import lombok.*;


public class BranchModel {
    private String name;
    private Commit commit;

    public String getName() {
        return name;
    }

    public Commit getCommit() {
        return commit;
    }

    @Data
    public static class Commit {
        private String sha;
    }

}
