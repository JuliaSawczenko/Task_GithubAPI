package com.task_githubapi.exception;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

@Aspect
@Component
public class ErrorHandlingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingAspect.class);

    @AfterThrowing(pointcut = "execution(* com.task_githubapi.service.*.*(..))", throwing = "exception")
    public void handleException(JoinPoint joinPoint, Exception exception) throws GitHubApiException {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String errorMessage = exception.getMessage();

        logException(methodName, className, errorMessage);

        if (exception instanceof UserNotFoundException) {
            logger.error("User not found: {}", errorMessage);
        } else if (exception instanceof HttpClientErrorException) {
            throw new GitHubApiException("Client error occurred.");
        } else if (exception instanceof HttpServerErrorException) {
            throw new GitHubApiException("Server error occurred.");
        } else if (exception instanceof RestClientException) {
            throw new GitHubApiException("Error connecting to GitHub API.");
        } else {
            throw new GitHubApiException("Unexpected error occurred.");
        }
    }

    private void logException(String methodName, String className, String errorMessage) {
        logger.error("Error in method {} of class {}: {}", methodName, className, errorMessage);
    }
}