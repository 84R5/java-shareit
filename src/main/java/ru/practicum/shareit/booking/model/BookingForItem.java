package ru.practicum.shareit.booking.model;

import java.time.LocalDateTime;

public interface BookingForItem {
    Long getId();

    Long getBookerId();

    Long getItemId();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();
}