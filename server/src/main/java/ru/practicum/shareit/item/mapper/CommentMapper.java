package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemDto(ItemMapper.toItemDto(comment.getItem()))
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, User author) {
        return Comment.builder().id(
                        commentDto.getId())
                .text(commentDto.getText())
                .item(ItemMapper.toItem(commentDto.getItemDto()))
                .author(author)
                .created(commentDto.getCreated())
                .build();
    }
}
