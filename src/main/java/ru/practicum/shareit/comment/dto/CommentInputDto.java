package ru.practicum.shareit.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentInputDto {

    @NotBlank
    private String text;
}