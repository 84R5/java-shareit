package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class UserDto {

    Long id;

    @NotNull
    @NotBlank
    String name;

    @NotNull
    @NotBlank
    @Email
    String email;
}