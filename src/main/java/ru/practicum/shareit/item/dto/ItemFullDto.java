package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.comment.dto.CommentFullDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;


@Data
@NoArgsConstructor
public class ItemFullDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserDto owner;
    private BookingItemDto nextBooking;
    private BookingItemDto lastBooking;
    private List<CommentFullDto> comments;

    public ItemFullDto(String name, String description, Boolean available, UserDto owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    public ItemFullDto(Long id, String name, String description, Boolean available, UserDto owner) {
        this(name, description, available, owner);
        this.id = id;
    }
}
