package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemResponseDto> search(String text);

    List<ItemResponseDto> getByUserId(Long userId);

    ItemResponseDto getById(Long userId, Long itemId);

    ItemResponseDto create(Long userId, ItemInputDto itemInputDto);

    ItemResponseDto update(Long userId, Long itemId, ItemInputDto itemInputDto);

    Item getItemById(Long itemId);

    CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto);
}