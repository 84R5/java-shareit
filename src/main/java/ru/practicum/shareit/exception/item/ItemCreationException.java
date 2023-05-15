package ru.practicum.shareit.exception.item;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemCreationException extends RuntimeException {

    public ItemCreationException(String name) {
        super(String.format("Item %s not created.", name));
        log.warn("Item {} not created.", name);
    }
}