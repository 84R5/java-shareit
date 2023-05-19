package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.dto.UserMapper;

public class ItemMapper {

    public static ItemFullDto mapToFullDto(Item item) {
        return new ItemFullDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                UserMapper.mapToShortDto(item.getOwner()));
    }

    public static ItemShortDto mapToShortDto(Item item) {
        return new ItemShortDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable());
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
