package ru.practicum.shareit.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentInputDto {

    @NotNull
    @NotBlank
    private String text;
}
