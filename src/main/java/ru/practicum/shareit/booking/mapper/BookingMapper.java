package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class BookingMapper {

    public static BookingDto mapToFullDto(Booking booking) {
        if (booking == null) return null;
        return BookingDto.builder()
                .id(booking.getId())
                .start(fromInstant(booking.getStart()))
                .end(fromInstant(booking.getEnd()))
                .booker(UserMapper.mapToFullDto(booking.getBooker()))
                .item(ItemMapper.mapToRequestDto(booking.getItem()))
                .status(booking.getStatus())
                .build();
    }

    public static BookingItemDto mapToRequestDto(Booking booking) {
        return BookingItemDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .bookerId(booking.getBooker().getId())
                .build();

    }

    public static Booking mapToBooking(BookingRequestDto bookingRequestDto, Booking booking) {
        if (bookingRequestDto == null && booking.getItem() == null && booking.getBooker() == null && booking.getStatus() == null) {
            return null;
        }
        assert bookingRequestDto != null;
        return Booking.builder()
                .start(bookingRequestDto.getStart())
                .end(bookingRequestDto.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(Status.WAITING).build();
    }

    private static LocalDateTime fromInstant(LocalDateTime instant) {
        return instant == null ? null : instant.truncatedTo(ChronoUnit.SECONDS);
    }

}
