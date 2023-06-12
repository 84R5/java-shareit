package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {

    BookingDto create(Long userId, BookingRequestDto bookingRequestDto);

    List<BookingDto> getBookingsOwner(Long userId, String state, Integer from, Integer size);

    BookingDto getBookingById(Long userId, Long bookingId);

    BookingDto update(Long ownerId, Long bookingId, boolean approved);

    List<BookingDto> getBookingsWhisState(Long userId, String state, Integer from, Integer size);
}