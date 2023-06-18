package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.exception.UnsupportedStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(HEADER) Long userId,
                                         @RequestBody @Valid BookingInputDto bookingRequestDto) {
        log.info("Received POST request to endpoint: '/bookings' " +
                "to create a request from a User with ID={}", userId);
        return bookingClient.create(userId, bookingRequestDto);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader(HEADER) Long ownerId,
                                         @PathVariable Long bookingId,
                                         @RequestParam(value = "approved") Boolean approved) {
        log.info("Received PATCH request to endpoint: '/bookings' to update the booking status " +
                "from a 'BookingId' with ID={}", bookingId);
        return bookingClient.approve(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(HEADER) Long userId,
                                          @PathVariable Long bookingId) {
        log.info("Received GET request to endpoint: '/bookings' to receive a reservation " +
                "from a 'BookingId' with ID={}", bookingId);
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(HEADER) Long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String state,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        State.from(state).orElseThrow(() -> new UnsupportedStateException("Unknown state: " + state));

        log.info("Received GET request to endpoint: '/bookings' to get a list of all the user's bookings" +
                " from a User with ID={} param STATE={}", userId, state);
        return bookingClient.getBookingsWhisState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsOwner(@RequestHeader(HEADER) Long userId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        State.from(state).orElseThrow(() -> new UnsupportedStateException("Unknown state: " + state));
        log.info("Received GET request to endpoint: '/bookings/owner' to receive a list " +
                "of all bookings of the user's belongings from a User with ID={} param STATE={}", userId, state);
        return bookingClient.getBookingsOwner(userId, state, from, size);
    }
}
