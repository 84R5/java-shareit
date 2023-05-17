package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;

import java.util.List;

public interface BookingService {

    List<BookingFullDto> getByBookerId(Long userId, String subState);

    List<BookingFullDto> getByOwnerId(Long ownerId, String subState);

    BookingFullDto getById(Long userId, Long itemId);

    BookingFullDto create(Long userId, BookingInputDto bookingInputDto);

    BookingFullDto approve(Long userId, Long bookingId, Boolean isApproved);

}
