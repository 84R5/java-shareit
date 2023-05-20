package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {

    List<BookingDto> getByBookerId(Long userId, String subState);

    List<BookingDto> getByOwnerId(Long ownerId, String subState);

    BookingDto getById(Long userId, Long itemId);

    BookingDto create(Long userId, BookingRequestDto bookingRequestDto);

    BookingDto approve(Long userId, Long bookingId, Boolean isApproved);

}
