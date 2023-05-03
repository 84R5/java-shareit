package ru.practicum.shareit.exception.user;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserCreationException extends RuntimeException {

    public UserCreationException(String name) {
        super(String.format("User %s not created.", name));
        log.warn("User {} not created.", name);
    }
}