package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentFullDto;
import ru.practicum.shareit.comment.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody ItemInputDto itemInputDto) {
        return itemService.create(userId, itemInputDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody ItemInputDto itemInputDto,
                          @PathVariable Long itemId) {
        return itemService.update(userId, itemId, itemInputDto);
    }
    @PostMapping("/{itemId}/comment")
    public CommentFullDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long itemId,
                                     @Valid @RequestBody CommentInputDto commentInputDto) {
        return itemService.addComment(userId, itemId, commentInputDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable Long itemId) {
        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }


}