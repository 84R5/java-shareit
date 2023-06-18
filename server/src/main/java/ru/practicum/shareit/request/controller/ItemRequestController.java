package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String HEADER = "X-Sharer-User-Id";

    private final RequestService requestService;

    @PostMapping
    public RequestDto create(@RequestHeader(HEADER) Long userId,
                             @RequestBody RequestDto requestDto) {
        return requestService.create(userId, requestDto);
    }

    @GetMapping
    public List<RequestDto> getAllOwnRequests(@RequestHeader(HEADER) Long userId) {
        return requestService.getAllOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllRequestsOthersUser(@RequestHeader(HEADER) Long userId,
                                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return requestService.getAllRequestsOthersUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequest(@RequestHeader(HEADER) Long userId,
                                 @PathVariable("requestId") Long requestId) {
        return requestService.getItemRequestById(userId, requestId);
    }


}