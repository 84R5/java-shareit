package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentFullDto;
import ru.practicum.shareit.comment.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemDto> search(String text);

    List<ItemDto> getByUserId(Long userId);

    ItemDto getById(Long userId, Long itemId);

    ItemDto create(Long userId, ItemInputDto itemInputDto);

    ItemDto update(Long userId, Long itemId, ItemInputDto itemInputDto);

    Item getItemById(Long itemId);

    CommentFullDto addComment(Long userId, Long itemId, CommentInputDto commentInputDto);
}