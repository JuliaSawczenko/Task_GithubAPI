package com.task_githubapi.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(final String username) {
        super("Not able to find a user with username: " + username);
    }
}
