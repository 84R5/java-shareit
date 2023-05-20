package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public List<BookingDto> getByBookerId(Long bookerId, String subState) {
        State state = getState(subState);
        User booker = userService.getUserById(bookerId);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStateCurrentOrderByStartDesc(booker.getId());
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndStatePastOrderByStartDesc(booker.getId());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStateFutureOrderByStartDesc(booker.getId());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(booker.getId(),
                        Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(booker.getId(),
                        Status.REJECTED);
                break;
        }

        List<BookingDto> result = bookings
                .stream()
                .map(BookingMapper::mapToFullDto)
                .collect(Collectors.toList());

        log.info("Found {} booking(s).", result.size());
        return result;
    }

    @Override
    public List<BookingDto> getByOwnerId(Long ownerId, String subState) {
        State state = getState(subState);
        User owner = userService.getUserById(ownerId);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(owner.getId());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerIdAndStateCurrentOrderByStartDesc(owner.getId());
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerIdAndStatePastOrderByStartDesc(owner.getId());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerIdAndStateFutureOrderByStartDesc(owner.getId());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStartDesc(owner.getId(),
                        Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStartDesc(owner.getId(),
                        Status.REJECTED);
                break;
        }

        List<BookingDto> result = bookings
                .stream()
                .map(BookingMapper::mapToFullDto)
                .collect(Collectors.toList());

        log.info("Found {} booking(s).", result.size());
        return result;
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        Booking booking = getBookingById(bookingId);
        User booker = booking.getBooker();
        User owner = userService.getUserById(booking.getItem().getOwner().getId());
        if (!booker.getId().equals(userId) && !owner.getId().equals(userId)) {
            throw new IllegalArgumentException("The booking can only be viewed " +
                    "by the author or the owner of the item.");
        }
        BookingDto result = bookingRepository.findById(bookingId)
                .map(BookingMapper::mapToFullDto)
                .orElseThrow(() -> new NullPointerException(String.format("Booking %d is not found.", bookingId)));
        log.info("Booking {} is found.", result.getId());
        return result;
    }

    @Transactional
    @Override
    public BookingDto create(Long userId, BookingRequestDto bookingRequestDto) {
        User booker = userService.getUserById(userId);
        Item item = itemService.getItemById(bookingRequestDto.getItemId());

        if (booker.getId().equals(item.getOwner().getId())) {
            throw new IllegalArgumentException("The owner cannot book his own things");
        }

        if (!item.isAvailable()) {
            throw new IllegalStateException(String.format("Item %d is unavailable.", item.getId()));
        }

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        BookingDto result =
                Optional.of(bookingRepository.save(BookingMapper.mapToBooking(bookingRequestDto, booking)))
                        .map(BookingMapper::mapToFullDto)
                        .orElseThrow();
        log.info("Booking {} {} created.", result.getId(), result.getItem().getName());
        return result;
    }

    @Transactional
    @Override
    public BookingDto approve(Long userId, Long bookingId, Boolean isApproved) {
        User owner = userService.getUserById(userId);
        Booking booking = getBookingById(bookingId);
        Item item = itemService.getItemById(booking.getItem().getId());
        Status newStatus = isApproved ? Status.APPROVED : Status.REJECTED;

        if (!booking.getStatus().equals(Status.WAITING) && owner.getId().equals(item.getOwner().getId())) {
            throw new IllegalStateException(String.format("Booking %d cannot be updated", bookingId));
        }
        if (!owner.getId().equals(item.getOwner().getId())) {
            throw new IllegalArgumentException("Only the owner of the item can confirm the booking.");
        }

        booking.setStatus(newStatus);
        log.info("Booking status changed to {}.", booking.getStatus());
        return BookingMapper.mapToFullDto(booking);
    }


    public Booking getBookingById(Long bookingId) {
        return bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new NullPointerException(String.format("Booking %d is not found.", bookingId)));
    }

    private State getState(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException(state);
        }
    }
}
