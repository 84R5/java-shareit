package ru.practicum.shareit.exception.user;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends NullPointerException {

    public UserNotFoundException(Long userId) {
        super(String.format("User %d is missing.", userId));
        log.warn("User {} is missing.", userId);
    }
}