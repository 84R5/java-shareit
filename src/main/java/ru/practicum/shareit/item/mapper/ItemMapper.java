package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.Collection;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(item.getId(),
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

    public static Collection<ItemDto> mapArrayToItemDto(Collection<Item> items){
        return items.stream().map(ItemMapper::mapToItemDto).collect(Collectors.toList());
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
