package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto create(Long userId, ItemDto itemDto);

    List<ItemDtoFull> getItemsByOwner(Long userId, Integer from, Integer size);

    ItemDtoFull getItemByIdFromUser(Long userId, Long itemId);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> getItemsBySearch(Long userId, String text, Integer from, Integer size);

    Item getItemById(Long itemId);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto);
}