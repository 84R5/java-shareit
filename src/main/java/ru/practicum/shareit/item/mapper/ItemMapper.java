package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(UserMapper.toUser(itemDto.getOwner()))
                .build();
    }

    public static ItemDtoWithDate toItemDtoWithDate(Item item) {
        return ItemDtoWithDate.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .available(item.getAvailable())
                .request(item.getRequest() != null ? RequestMapper.toItemRequestDto(item.getRequest()) : null)
                .build();
    }
}
/*
package ru.practicum.shareit.item.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@Component
public class ItemMapper {


    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                UserMapper.toUserDto(item.getOwner()),
                item.getRequest() != null ? item.getRequest().getId() : null,
                null
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                UserMapper.toUser(itemDto.getOwner()),
                null
        );
    }

    public static ItemDtoWithDate toItemDtoWithDate(Item item) {
        return new ItemDtoWithDate(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                UserMapper.toUserDto(item.getOwner()),
                item.getRequest() != null ? RequestMapper.toItemRequestDto(item.getRequest()) : null,
                null,
                null,
                null
        );
    }



}
*/
