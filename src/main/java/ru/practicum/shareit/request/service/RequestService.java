package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto create(Long userId, RequestDto requestDto);

    List<RequestDto> getAllOwnRequests(Long userId);

    List<RequestDto> getAllRequestsOthersUser(Long userId, Integer from, Integer size);

    RequestDto getItemRequestById(Long userId, Long requestId);
}

/*
package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

import java.awt.*;
import java.util.Collection;

public interface RequestService {
    RequestDto create(Long userId, RequestDto dto);

    RequestDto getRequestById(Long userId, Long requestId);

    Collection<RequestDto> getRequestByUserId(Long userId);

    Collection<RequestDto> getRequestAll(Long userId, Integer from, Integer size);
}
*/
