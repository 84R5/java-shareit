package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoToCreate;
import ru.practicum.shareit.request.model.Request;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    public static RequestDto toItemRequestDto(Request request){
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .timeCreation(request.getTimeCreate())
                .items((List<ItemDto>) ItemMapper.mapArrayToItemDto(request.getItems()))
                .build();
    }

    public static RequestDtoToCreate toItemRequestDtoToCreate(Request request){
        return RequestDtoToCreate.builder()
                .id(request.getId())
                .description(request.getDescription())
                .timeCreation(request.getTimeCreate())
                .build();
    }

    public static Request toItemRequest(Request request){
        return Request.builder()
                .id(request.getId())
                .description(request.getDescription())
                .build();
    }

    public static Collection<RequestDto> arrayToItemRequestDto(Collection<Request> requests){
        return requests.stream().map(RequestMapper::toItemRequestDto).collect(Collectors.toList());
    }
}
