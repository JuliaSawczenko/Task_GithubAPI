package com.task_githubapi.exception;

public class GitHubApiException extends Exception {

    public GitHubApiException() {
        super("An error occurred while accessing GitHub services");
    }
}
