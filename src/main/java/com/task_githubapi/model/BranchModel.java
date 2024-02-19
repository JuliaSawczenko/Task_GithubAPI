package com.task_githubapi.model;


public class BranchModel {
    private String name;
    private Commit commit;

    public String getName() {
        return name;
    }

    public Commit getCommit() {
        return commit;
    }

    public static class Commit {
        private String sha;

        public String getSha() {
            return sha;
        }
    }

}
