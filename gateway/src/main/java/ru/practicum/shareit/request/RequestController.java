package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestInputDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Validated
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {

    private static final String HEADER = "X-Sharer-User-Id";

    private final RequestClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(HEADER) Long userId,
                                         @RequestBody @Valid RequestInputDto requestDto) {
        return client.create(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnRequests(@RequestHeader(HEADER) Long userId) {
        return client.getAllOwnRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsOthersUser(@RequestHeader(HEADER) Long userId,
                                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                           @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return client.getAllRequestsOthersUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(HEADER) Long userId,
                                                     @PathVariable("requestId") Long requestId) {
        return client.getRequestById(userId, requestId);
    }


}