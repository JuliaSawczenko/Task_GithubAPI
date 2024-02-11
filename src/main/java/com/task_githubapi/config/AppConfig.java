package com.task_githubapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static final String gitHubToken = System.getenv("GITHUB_JWT");

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            request.getHeaders().set("Authorization", "Bearer " + gitHubToken);
            request.getHeaders().set("Accept", "application/vnd.github+json");
            try {
                return execution.execute(request, body);
            } catch (HttpClientErrorException e) {
                logger.error("HttpClientErrorException: Status Code: {}", e.getStatusCode(), e);
                throw e;
            } catch (Exception e) {
                logger.error("Exception occurred while sending request to GitHub: ", e);
                throw e;
            }
        };

        restTemplate.getInterceptors().add(interceptor);
        return restTemplate;
    }
}
