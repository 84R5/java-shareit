package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validators.StartEndDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor

@StartEndDate
public class BookingInputDto {

    @Future
    @NotNull
    private LocalDateTime start;

    @Future
    @NotNull
    private LocalDateTime end;

    @NotNull
    private Long itemId;
}
