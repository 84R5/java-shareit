package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto create(Long userId, RequestDto requestDto);

    List<RequestDto> getAllOwnRequests(Long userId);

    List<RequestDto> getAllRequestsOthersUser(Long userId, Integer from, Integer size);

    RequestDto getItemRequestById(Long userId, Long requestId);
}