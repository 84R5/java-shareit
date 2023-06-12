package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ErrorTest {
    private ErrorHandler errorHandler;

    @BeforeEach
    public void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    public void handleUnsupportedStateException_ReturnsInternalServerError() {
        UnsupportedStateException ex = new UnsupportedStateException("Unsupported state");
        ResponseEntity<ErrorResponse> responseEntity = errorHandler.handleUnsupportedStateException(ex);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void notFoundBookingException_HasResponseStatusNotFound() {
        ObjectNotFoundException ex = new ObjectNotFoundException("Object not found");
        ResponseEntity<ErrorResponse> responseEntity = errorHandler.handleRequestNotFoundException(ex);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void createNotFoundObjectException() {
        String message = "Object not found";
        ObjectNotFoundException exception = new ObjectNotFoundException(message);

        Assertions.assertEquals(message, exception.getMessage());
    }

    @Test
    public void unsupportedStateException() {
        String message = "Unsupported state";
        UnsupportedStateException exception = new UnsupportedStateException(message);

        Assertions.assertEquals(message, exception.getMessage());
    }
}