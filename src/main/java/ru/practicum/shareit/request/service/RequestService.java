package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoToCreate;

import java.util.Collection;

public interface RequestService {
    RequestDtoToCreate create(long userId, RequestDto requestDto);

    RequestDto getRequestById(long userId, long requestId);

    Collection<RequestDto> getRequestByUserId(long userId);

    Collection<RequestDto> getRequestAll(long userId, int from, int size);
}
