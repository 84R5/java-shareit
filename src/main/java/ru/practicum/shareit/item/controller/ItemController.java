package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @Valid @RequestBody ItemInputDto itemInputDto) {
        return itemService.create(userId, itemInputDto);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestBody ItemInputDto itemInputDto,
                                  @PathVariable Long itemId) {
        return itemService.update(userId, itemId, itemInputDto);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto get(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long itemId) {
        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemResponseDto> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long itemId,
                                         @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return itemService.addComment(userId, itemId, commentRequestDto);
    }
}