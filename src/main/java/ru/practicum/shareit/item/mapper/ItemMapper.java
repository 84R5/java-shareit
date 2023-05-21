package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    static CommentMapper commentMapper;
    static BookingMapper bookingMapper;

    public static ItemResponseDto mapToItemDto(Item item) {
        if (item == null) {
            return null;
        }
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .owner(item.getOwner())
                .comments(commentMapper.mapToCommentDtoList(item.getComments()))
                .lastBooking(bookingMapper.toBookingItemDto(item.getLastBooking()))
                .nextBooking(bookingMapper.toBookingItemDto(item.getNextBooking()))
                .build();
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
