package com.task_githubapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GitHubControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getRepositoriesByUsername_ReturnsRepositories() throws Exception {
        String username = "JuliaSawczenko";

        mockMvc.perform(get("/github/user/{username}/repositories", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].name", is(not(emptyOrNullString()))))
                .andExpect(jsonPath("$[0].ownerLogin", is(username)))
                .andExpect(jsonPath("$[*].branches[?(@.name)].name", everyItem(not(emptyOrNullString()))))
                .andExpect(jsonPath("$[*].branches[*].lastCommit", everyItem(not(emptyOrNullString()))))

                //Specific checks for JuliaSawczenko user - modify when changing the user
                .andExpect(jsonPath("$[?(@.name == 'Amazing-Numbers')].branches[0].name", contains("main")))
                .andExpect(jsonPath("$[?(@.name == 'Amazing-Numbers')].branches[0].lastCommit", contains("1a7335a9aab579bf4df955025852953dd2e348b2")));
    }

    @Test
    public void getUserByUsername_NotFound_Returns404WithError() throws Exception {
        String nonExistentUsername = "JuliaSawczenk9o";

        mockMvc.perform(get("/github/user/{username}/repositories", nonExistentUsername)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Not able to find a user with username: " + nonExistentUsername)));
    }

    @Test
    public void requestWithUnsupportedAcceptHeader_ReturnsError() throws Exception {
        String username = "JuliaSawczenko";

        mockMvc.perform(get("/github/user/{username}/repositories", username)
                        .header("Accept", "text/plain"))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.error", is("This API only supports application/json.")));
    }
}
