package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentFullDto {

    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;
}