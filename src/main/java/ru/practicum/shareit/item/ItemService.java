package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    List<ItemDto> search(String text);

    List<ItemDto> getByUserId(Long userId);

    ItemDto getById(Long itemId);

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);



}