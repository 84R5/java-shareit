package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentFullDto;
import ru.practicum.shareit.comment.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemFullDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody ItemInputDto itemInputDto) {
        return itemService.create(userId, itemInputDto);
    }

    @PatchMapping("/{itemId}")
    public ItemFullDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemInputDto itemInputDto,
                              @PathVariable Long itemId) {
        return itemService.update(userId, itemId, itemInputDto);
    }

    @GetMapping("/{itemId}")
    public ItemFullDto get(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId) {
        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemFullDto> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemFullDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentFullDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long itemId,
                                     @Valid @RequestBody CommentInputDto commentInputDto) {
        return itemService.addComment(userId, itemId, commentInputDto);
    }
}