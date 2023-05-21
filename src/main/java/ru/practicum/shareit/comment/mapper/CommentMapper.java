package ru.practicum.shareit.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CommentMapper {

    public static CommentResponseDto mapToCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public Comment mapToComment(CommentRequestDto dto, User user, Item item, LocalDateTime time) {
        return Comment.builder()
                .id(dto.getId())
                .text(dto.getText())
                .item(item)
                .author(user)
                .created(time)
                .build();
    }

    public List<CommentResponseDto> mapToCommentDtoList(List<Comment> comments) {
        if (comments == null) {
            return Collections.emptyList();
        }

        List<CommentResponseDto> list = new ArrayList<>(comments.size());
        for (Comment comment : comments) {
            list.add(mapToCommentResponseDto(comment));
        }

        return list;
    }
}
