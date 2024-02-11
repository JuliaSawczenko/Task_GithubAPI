package com.task_githubapi.exception;

import com.task_githubapi.dto.ExceptionMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHttpErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalHttpErrorHandler.class);


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionMessageDTO> handleUserNotFoundException(UserNotFoundException exception) {
        logException(exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionMessageDTO(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }

    @ExceptionHandler(GitHubApiException.class)
    public ResponseEntity<ExceptionMessageDTO> handleGitHubApiException(GitHubApiException exception) {
        logException(exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }

    private void logException(Exception exception) {
        logger.error("Exception occurred: ", exception);
    }


}
