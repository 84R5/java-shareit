package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.mapper.UserMapper;

public class RequestMapper {
    public static Request toItemRequest(RequestDto requestDto) {
        return Request.builder()
                .id(requestDto.getId())
                .description(requestDto.getDescription())
                .requester(UserMapper.toUser(requestDto.getRequester()))
                .created(requestDto.getCreated())
                .build();
    }

    public static RequestDto toItemRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requester(UserMapper.toUserDto(request.getRequester()))
                .created(request.getCreated())
                .build();
    }
}