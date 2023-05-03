package ru.practicum.shareit.exception.user;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailConflictException extends RuntimeException {

    public EmailConflictException(String email) {
        super(String.format("Email %s is not added.", email));
        log.warn("Email {} is not added.", email);
    }
}