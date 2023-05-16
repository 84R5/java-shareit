package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemInputDto;

import javax.xml.stream.events.Comment;
import java.util.List;

public interface ItemService {

    List<ItemFullDto> search(String text);

    List<ItemFullDto> getByUserId(Long userId);

    ItemFullDto getById(Long userId,Long itemId);

    ItemFullDto create(Long userId, ItemInputDto itemDto);

    ItemFullDto update(Long userId, Long itemId, ItemInputDto itemDto);

    Item getItemById(Long itemId);

    CommentFullDto addComment(Long userId, Long itemId, CommentInputDto commentInputDto);
}