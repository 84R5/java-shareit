package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .item(ItemMapper.toItemDto(booking.getItem()))
                .status(booking.getStatus().toString())
                .build();
    }

    public static BookingRequestDto toBookingRequestDto(Booking booking) {
        return BookingRequestDto.builder().id(booking.getId()).start(booking.getStart()).end(booking.getEnd())
                .bookerId(booking.getBooker().getId()).itemId(booking.getItem().getId()).status(booking.getStatus().toString()).build();
    }
}

