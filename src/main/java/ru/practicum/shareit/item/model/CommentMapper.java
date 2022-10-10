package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.CommentDto;
/**
 * Маппер комментария
 */
public class CommentMapper {
    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                null,
                null,
                commentDto.getCreated());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
}
