package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> findByUserId(Long userId);

    Optional<Item> findById(Long itemId);

    Optional<Item> create(Item item);

    Optional<Item> update(Item item);

    List<Item> search(String text);
}