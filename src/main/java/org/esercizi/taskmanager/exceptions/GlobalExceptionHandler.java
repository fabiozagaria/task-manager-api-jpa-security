package org.esercizi.taskmanager.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<APIError> handleTaskNotFound(
            TaskNotFoundException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(
                        new APIError(
                        "TASK_NOT_FOUND",
                        exception.getMessage(),
                        request.getRequestURI(),
                        status

                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIError> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {

        return ResponseEntity
                .badRequest().body(
                        new APIError(
                        "ILLEGAL_ARGUMENT",
                        exception.getMessage(),
                        request.getRequestURI(),
                        HttpStatus.BAD_REQUEST
                )
                );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIError> handleDataInt(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity
                .status(status)
                .body(
                        new APIError(
                                "USER_CONFLICT",
                                "Username already exists",
                                request.getRequestURI(),
                                status
                                )
                );

    }
}
