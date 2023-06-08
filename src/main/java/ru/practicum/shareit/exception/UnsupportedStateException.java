package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnsupportedStateException extends RuntimeException {

    public UnsupportedStateException(String message) {
        super(message);
        log.warn("Unsupported State: {}" + message);

    }
}
