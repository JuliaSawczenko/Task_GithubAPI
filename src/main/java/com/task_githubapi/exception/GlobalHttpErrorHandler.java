package com.task_githubapi.exception;

import com.task_githubapi.dto.ExceptionMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionMessageDTO> handleUserNotFoundException(UserNotFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new ExceptionMessageDTO(status.value(), exception.getMessage()));
    }

    @ExceptionHandler(GitHubApiException.class)
    public ResponseEntity<ExceptionMessageDTO> handleGitHubApiException(GitHubApiException exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new ExceptionMessageDTO(status.value(), exception.getMessage()));
    }
}
