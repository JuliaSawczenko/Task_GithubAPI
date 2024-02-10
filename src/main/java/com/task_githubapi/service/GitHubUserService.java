package com.task_githubapi.service;

import com.task_githubapi.exception.GitHubApiException;
import com.task_githubapi.exception.UserNotFoundException;
import com.task_githubapi.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GitHubUserService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubUserService.class);
    private final RestTemplate restTemplate;

    public GitHubUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void getUserByUsername(final String username) throws UserNotFoundException, GitHubApiException {
        String url = "https://api.github.com/users/" + username;
        logger.info("Fetching GitHub user: {}", username);

        try {
            ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
            logger.info("Successfully fetched user: {}", username);
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("User not found: {}", username, e);
            throw new UserNotFoundException(username);
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching user from GitHub: {}", username, e);
            throw new GitHubApiException();
        }
    }
}