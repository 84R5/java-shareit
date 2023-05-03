package ru.practicum.shareit.exception.item;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemNotFoundException extends NullPointerException {

    public ItemNotFoundException(Long itemId) {
        super(String.format("Item %d is not found.", itemId));
        log.warn("Item {} is not found.", itemId);
    }
}