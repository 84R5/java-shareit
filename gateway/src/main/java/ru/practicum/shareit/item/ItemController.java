package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER = "X-Sharer-User-Id";
    private final ItemClient client;

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(HEADER) Long userId,
                                         @RequestBody @Valid ItemInputDto itemDto) {
        //log.info("Получен POST-запрос к эндпоинту: '/items' на добавление вещи владельцем с ID={}", userId);
        return client.create(userId, itemDto);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(HEADER) Long userId,
                                                @RequestBody @Valid CommentInputDto commentDto,
                                                @PathVariable("itemId") Long itemId) {
        //log.info("Получен POST-запрос к эндпоинту: '/items/comment' на" +
        //        " добавление отзыва пользователем с ID={}", userId);
        return client.createComment(userId, itemId, commentDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(HEADER) Long ownerId,
                                                  @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        //log.info("Получен GET-запрос к эндпоинту: '/items' на получение всех вещей владельца с ID={}", ownerId);
        return client.getItemsByOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearchQuery(@RequestHeader(HEADER) Long userId,
                                                        @RequestParam String text,
                                                        @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        //log.info("Получен GET-запрос к эндпоинту: '/items/search' на поиск вещи с текстом={}", text);
        return client.getItemsBySearch(userId, text, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemByIdFromUser(@RequestHeader(HEADER) Long userId,
                                                      @PathVariable("itemId") Long itemId) {
        //log.info("Получен GET-запрос к эндпоинту: '/items' на получение вещи с ID={}", itemId);
        return client.getItemByIdFromUser(userId, itemId);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(HEADER) Long userId,
                                         @RequestBody ItemInputDto itemDto,
                                         @PathVariable Long itemId) {
        //log.info("Получен PATCH-запрос к эндпоинту: '/items' на обновление вещи с ID={}", itemId);
        return client.update(userId, itemId, itemDto);
    }
}
