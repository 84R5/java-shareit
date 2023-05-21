package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemForBookingDto {

    long id;
    String name;
}
