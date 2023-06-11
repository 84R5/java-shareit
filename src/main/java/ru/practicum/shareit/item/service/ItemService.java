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

/*
package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long ownerId);

    Item findItem(Long itemId);

    ItemDtoWithDate getItemByIdFromUser(Long id, Long userId);

    ItemDto update(ItemDto itemDto, Long ownerId, Long itemId);

    void delete(Long itemId, Long ownerId);

    List<ItemDtoWithDate> getItemsByOwner(Long ownerId, Integer from, Integer size);

    List<ItemDtoWithDate> getItemsByOwner(Long userId);

    List<ItemDto> getItemsBySearch(Long userId, String query, Integer from, Integer size);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);

}*/
