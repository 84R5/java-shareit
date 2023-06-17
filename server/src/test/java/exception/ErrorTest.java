package exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.ObjectNotFoundException;

class ErrorTest {
    private ErrorHandler errorHandler;

    @BeforeEach
    public void setUp() {
        errorHandler = new ErrorHandler();
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
}