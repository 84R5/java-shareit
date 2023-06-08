package ru.practicum.shareit.item.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @ResponseBody
    @PostMapping
    public ItemDto create(@RequestHeader(HEADER) Long userId,
                          @RequestBody @Valid ItemDto itemDto) {
        log.info("Получен POST-запрос к эндпоинту: '/items' на добавление вещи владельцем с ID={}", userId);
        return itemService.create(userId, itemDto);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(HEADER) Long userId,
                                    @RequestBody @Valid CommentDto commentDto,
                                    @PathVariable("itemId") Long itemId) {
        log.info("Получен POST-запрос к эндпоинту: '/items/comment' на" +
                " добавление отзыва пользователем с ID={}", userId);
        return itemService.createComment(userId, itemId, commentDto);
    }

    @GetMapping
    public List<ItemDtoWithDate> getItemsByOwner(@RequestHeader(HEADER) Long ownerId,
                                                 @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос к эндпоинту: '/items' на получение всех вещей владельца с ID={}", ownerId);
        return itemService.getItemsByOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearchQuery(@RequestHeader(HEADER) Long userId,
                                               @RequestParam String text,
                                               @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                               @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос к эндпоинту: '/items/search' на поиск вещи с текстом={}", text);
        return itemService.getItemsBySearch(userId, text, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithDate getItemByIdFromUser(@RequestHeader(HEADER) Long userId,
                                               @PathVariable("itemId") Long itemId) {
        log.info("Получен GET-запрос к эндпоинту: '/items' на получение вещи с ID={}", itemId);
        return itemService.getItemByIdFromUser(userId, itemId);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER) Long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Long itemId) {
        log.info("Получен PATCH-запрос к эндпоинту: '/items' на обновление вещи с ID={}", itemId);
        return itemService.update(userId, itemId, itemDto);
    }
}
