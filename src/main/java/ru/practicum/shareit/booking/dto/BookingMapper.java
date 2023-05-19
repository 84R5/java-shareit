package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

public class BookingMapper {

    public static BookingFullDto mapToFullDto(Booking booking) {
        return new BookingFullDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.mapToShortDto(booking.getItem()),
                UserMapper.mapToShortDto(booking.getBooker()),
                booking.getStatus());
    }

    public static BookingShortDto mapToShortDto(Booking booking) {
        return new BookingShortDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker().getId());
    }

    public static Booking mapToBooking(BookingInputDto bookingInputDto, Booking booking) {
        booking.setStart(bookingInputDto.getStart());
        booking.setEnd(bookingInputDto.getEnd());
        booking.setStatus(Status.WAITING);

        return booking;
    }
}
