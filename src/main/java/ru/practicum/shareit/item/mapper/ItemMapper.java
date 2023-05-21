package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

public class ItemMapper {

    public static ItemFullDto mapToFullDto(Item item) {
        return new ItemFullDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                UserMapper.mapToFullDto(item.getOwner()));
    }

    public static ItemRequestDto mapToRequestDto(Item item) {
        return ItemRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }


    public static Item mapToItem(ItemInputDto itemInputDto, Item item) {

        if (itemInputDto.getName() != null) {
            item.setName(itemInputDto.getName());
        }

        if (itemInputDto.getDescription() != null) {
            item.setDescription(itemInputDto.getDescription());
        }

        if (itemInputDto.getAvailable() != null) {
            item.setAvailable(itemInputDto.getAvailable());
        }

        return item;
    }

}
