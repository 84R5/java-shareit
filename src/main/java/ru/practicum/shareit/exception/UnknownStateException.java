package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnknownStateException extends IllegalStateException {

    public UnknownStateException(String text) {
        super(String.format("Unknown state: %s", text));
        log.warn("Unknown state: {}", text);
    }
}
