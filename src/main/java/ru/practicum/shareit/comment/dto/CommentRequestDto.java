package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class CommentRequestDto {

    long id;
    @NotNull
    @NotBlank String text;
}
