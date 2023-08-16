package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.DTO.CommentCreationDto;
import ru.practicum.DTO.CommentGetDto;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CommentMapper {

    public static Comment dtoToComment(User user, Event event, CommentCreationDto commentCreationDto) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setText(commentCreationDto.getText());
        comment.setCommentator(user);
        comment.setEvent(event);
        return comment;
    }

    public static CommentGetDto commentToGetDto(Comment comment) {
        return new CommentGetDto(comment.getId(), comment.getCreated(), comment.getEvent().getId(), comment.getCommentator().getId(), comment.getText());
    }

}
