package ru.practicum.shareit.exception.item;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WrongOwnerException extends RuntimeException {

    public WrongOwnerException(Long userId, Long itemId) {
        super(String.format("User %d is not the owner of the item %d.", userId, itemId));
        log.warn("User {} is not the owner of the item {}.", userId, itemId);
    }
}