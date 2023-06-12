package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String HEADER = "X-Sharer-User-Id";
    private final BookingService service;

    @ResponseBody
    @PostMapping
    public BookingDto create(@RequestHeader(HEADER) Long userId,
                             @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        log.info("Received POST request to endpoint: '/bookings' " +
                "to create a request from a User with ID={}", userId);
        return service.create(userId, bookingRequestDto);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestHeader(HEADER) Long ownerId,
                             @PathVariable Long bookingId,
                             @RequestParam(value = "approved") Boolean approved) {
        log.info("Received PATCH request to endpoint: '/bookings' to update the booking status " +
                "from a 'BookingId' with ID={}", bookingId);
        return service.update(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(HEADER) Long userId,
                                     @PathVariable Long bookingId) {
        log.info("Received GET request to endpoint: '/bookings' to receive a reservation " +
                "from a 'BookingId' with ID={}", bookingId);
        return service.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader(HEADER) Long userId,
                                        @RequestParam(name = "state", defaultValue = "ALL") String state,
                                        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(name = "size", defaultValue = "20") @Positive Integer size) {
        log.info("Received GET request to endpoint: '/bookings' to get a list of all the user's bookings" +
                " from a User with ID={} param STATE={}", userId, state);
        return service.getBookingsWhisState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsOwner(@RequestHeader(HEADER) Long userId,
                                             @RequestParam(name = "state", defaultValue = "ALL") String state,
                                             @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(name = "size", defaultValue = "20") @Positive Integer size) {
        log.info("Received GET request to endpoint: '/bookings/owner' to receive a list " +
                "of all bookings of the user's belongings from a User with ID={} param STATE={}", userId, state);
        return service.getBookingsOwner(userId, state, from, size);
    }
}
