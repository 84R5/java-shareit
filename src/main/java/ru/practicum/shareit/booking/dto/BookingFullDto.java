package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class BookingFullDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private ItemShortDto item;
    private UserShortDto user;
}
