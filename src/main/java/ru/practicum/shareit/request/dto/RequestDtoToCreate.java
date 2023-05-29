package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RequestDtoToCreate {

    long id;
    @NotEmpty String description;
    LocalDateTime timeCreation;

}
