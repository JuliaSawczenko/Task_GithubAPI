package com.task_githubapi.service;

import com.task_githubapi.exception.GitHubApiException;
import com.task_githubapi.exception.UserNotFoundException;
import com.task_githubapi.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.task_githubapi.mocks.MockedValues.USERNAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubUserServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GitHubUserService gitHubUserService;

    @Test
    void getUserByUsername_whenUserExists_doesNotThrowException() {
        // Given
        when(restTemplate.getForEntity(anyString(), eq(User.class)))
                .thenReturn(new ResponseEntity<>(new User(), HttpStatus.OK));

        // When & Then
        assertDoesNotThrow(() -> gitHubUserService.getUserByUsername(USERNAME));
    }

    @Test
    void getUserByUsername_whenUserNotFound_thenThrowUserNotFoundException() {
        // Given
        when(restTemplate.getForEntity(anyString(), eq(User.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // When & Then
        assertThrows(UserNotFoundException.class,
                () -> gitHubUserService.getUserByUsername(USERNAME));
    }


    @Test
    void getUserByUsername_whenHttpClientErrorException_thenHandleAccordingly() {
        // Given
        when(restTemplate.getForEntity(anyString(), eq(User.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // When & Then
        assertThrows(GitHubApiException.class,
                () -> gitHubUserService.getUserByUsername(USERNAME));
    }
}
