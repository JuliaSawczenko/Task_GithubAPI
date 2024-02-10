package com.task_githubapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_githubapi.dto.RepositoryDTO;
import com.task_githubapi.exception.UserNotFoundException;
import com.task_githubapi.mocks.RepositoryDTOMock;
import com.task_githubapi.service.GitHubRepoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static com.task_githubapi.mocks.MockedValues.EXCEPTION_MESSAGE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.util.List;

import static com.task_githubapi.mocks.MockedValues.USERNAME;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(GitHubController.class)
public class GitHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubRepoService gitHubRepoService;


        @Test
        @WithMockUser(username = USERNAME)
        @DisplayName("GET /github/user/{username}/repositories returns a list of a user's repositories")
        void getRepositoriesByUsername_success() throws Exception {
            // Given
            List<RepositoryDTO> mockRepositoryDTOs = RepositoryDTOMock.getRepositoryDTO();
            when((gitHubRepoService).assembleRepositoriesWithBranches(anyString())).thenReturn(mockRepositoryDTOs);

            ObjectMapper objectMapper = new ObjectMapper();
            String expectedResponseBody = objectMapper.writeValueAsString(mockRepositoryDTOs);

            // When & Then
            mockMvc.perform(get("/github/user/{username}/repositories", USERNAME)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponseBody));

            verify(gitHubRepoService).assembleRepositoriesWithBranches(USERNAME);

        }

    @Test
    @WithMockUser(username = USERNAME)
    @DisplayName("GET /github/user/{username}/repositories returns 404 for nonexistent user")
    void getRepositoriesByUsername_userNotFound() throws Exception {
        // Given
        String expectedMessage = EXCEPTION_MESSAGE + USERNAME;
        when(gitHubRepoService.assembleRepositoriesWithBranches(anyString()))
                .thenThrow(new UserNotFoundException(USERNAME));

        // When & Then
        mockMvc.perform(get("/github/user/{username}/repositories", USERNAME)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(expectedMessage));

        verify(gitHubRepoService).assembleRepositoriesWithBranches(USERNAME);
    }
}
