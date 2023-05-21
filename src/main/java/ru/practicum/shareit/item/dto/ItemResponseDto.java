package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Builder
@Data
public class ItemResponseDto {

    Long id;
    @NotBlank String name;
    @NotBlank String description;
    @NotNull Boolean available;
    User owner;

    BookingItemDto nextBooking;

    BookingItemDto lastBooking;
    @With
    List<CommentResponseDto> comments;

}
