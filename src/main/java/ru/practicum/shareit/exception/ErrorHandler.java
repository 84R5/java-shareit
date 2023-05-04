package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.item.ItemCreationException;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.item.WrongOwnerException;
import ru.practicum.shareit.exception.user.EmailConflictException;
import ru.practicum.shareit.exception.user.UserCreationException;
import ru.practicum.shareit.exception.user.UserNotFoundException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({UserNotFoundException.class, ItemNotFoundException.class, WrongOwnerException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(final RuntimeException e) {
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler({UserCreationException.class, ItemCreationException.class, EmailConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final UserCreationException e) {
        return new ErrorResponse("error", e.getMessage());
    }
}
