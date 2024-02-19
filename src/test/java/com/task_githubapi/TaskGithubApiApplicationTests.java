package com.task_githubapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.task_githubapi.dto.RepositoryDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.IOException;


import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TaskGithubApiApplicationTests {

	private static WireMockServer wireMockServer;

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ObjectMapper objectMapper;

	@DynamicPropertySource
	static void configure(DynamicPropertyRegistry registry) {
		registry.add("github.api.base.url", () -> "http://localhost:" + wireMockServer.port());
		registry.add("github.token", () -> "dummy");
	}

	@BeforeAll
	static void startWireMock() {
		wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort().notifier(new ConsoleNotifier(true)));
		wireMockServer.start();
		configureFor("localhost", wireMockServer.port());
	}

	@AfterAll
	static void stopWireMock() {
		if (wireMockServer != null) {
			wireMockServer.stop();
		}
	}

	@Test
	void testUserRepos() throws IOException {

		// Given: Setup WireMock to respond with a successful response for repository request
		wireMockServer.stubFor(get(urlPathEqualTo("/users/userLogin/repos"))
				.withHeader("Authorization", equalTo("Bearer dummy"))
				.withHeader("Accept", equalTo("application/vnd.github+json"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBodyFile("github_repos_response.json")));


		wireMockServer.stubFor(get(urlPathMatching("/repos/userLogin/repoName/branches"))
				.withHeader("Authorization", equalTo("Bearer dummy"))
				.withHeader("Accept", equalTo("application/vnd.github+json"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBodyFile("github_branch_response.json")));


		// Expected: Fetching user repositories should succeed with a correct response
		File jsonFile = new ClassPathResource("__files/expected_controller_response.json").getFile();
		RepositoryDTO[] repositoryDTOS = objectMapper.readValue(jsonFile, RepositoryDTO[].class);

		webTestClient.get().uri("/users/userLogin/repos")
				.header("Authorization", "Bearer dummy")
				.header("Accept", "application/json")
				.exchange()
				.expectStatus().isOk()
				.expectBody(RepositoryDTO[].class).isEqualTo(repositoryDTOS);

	}

	@Test
	void testUserRepos_UserDoesNotExist() {

		// Given: Setup WireMock for a user that does not exist
		wireMockServer.stubFor(get(urlPathEqualTo("/users/nonexistentuser/repos"))
				.withHeader("Authorization", equalTo("Bearer dummy"))
				.withHeader("Accept", equalTo("application/vnd.github+json"))
				.willReturn(aResponse()
						.withStatus(404)
						.withHeader("Content-Type", "application/json")
						.withBody("{\"status\": 404, \"message\": \"Not able to find a user with username: " + "nonexistentuser" + "\"}")));


		// Expected: Fetching repositories for a nonexistent user should return 404
		webTestClient.get().uri("/users/nonexistentuser/repos")
				.header("Authorization", "Bearer dummy")
				.header("Accept", "application/json")
				.exchange()
				.expectStatus().isNotFound()
				.expectBody()
				.jsonPath("$.status").isEqualTo(404)
				.jsonPath("$.message").isEqualTo("Not able to find a user with username: " + "nonexistentuser");

		verify(getRequestedFor(urlPathEqualTo("/users/" + "nonexistentuser" + "/repos")));
	}

	@Test
	void testRepositoryBranches_RepositoryNotFound() {

		// Given: Setup WireMock for a repository that does not exist
		wireMockServer.stubFor(get(urlPathMatching("/repos/userLogin/nonExistentRepo/branches"))
				.willReturn(aResponse()
						.withStatus(404)
						.withHeader("Content-Type", "application/json")
						.withBody("{\"error\": \"Not Found\"}")));


		// Expected: Fetching branches for a nonexistent repository should return 404
		webTestClient.get().uri("/repos/userLogin/nonExistentRepo/branches")
				.header("Authorization", "Bearer dummy")
				.header("Accept", "application/json")
				.exchange()
				.expectStatus().isNotFound()
				.expectBody()
				.jsonPath("$.error").isEqualTo("Not Found");
	}
}
