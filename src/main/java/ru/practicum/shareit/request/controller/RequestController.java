package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoToCreate;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestDtoToCreate create(@Valid @RequestBody RequestDto dto,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.create(userId, dto);
    }

    @GetMapping
    public Collection<RequestDto> getRequestByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequestByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<RequestDto> getRequestAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size
    ) {
        return requestService.getRequestAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long requestId) {
        return requestService.getRequestById(userId, requestId);
    }

}
