package com.task_githubapi.service;

import com.task_githubapi.exception.UserNotFoundException;
import com.task_githubapi.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service
public class GitHubUserService {

    private final RestTemplate restTemplate;

    public GitHubUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void getUserByUsername(final String username) throws UserNotFoundException {
        String url = "https://api.github.com/users/" + username;

        try {
            restTemplate.getForEntity(url, User.class);
        } catch (HttpClientErrorException.NotFound e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException(username);
            }
        }
    }
}