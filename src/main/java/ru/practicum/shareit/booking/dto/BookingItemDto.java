package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingItemDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private Long bookerId;
}
