package ru.practicum.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.CommentCreationDto;
import ru.practicum.DTO.CommentGetDto;
import ru.practicum.exceptions.NotFoundEntityException;
import ru.practicum.exceptions.WrongParamsException;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.storage.CommentRepository;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.DateUtils.*;
import static ru.practicum.mapper.CommentMapper.commentToGetDto;
import static ru.practicum.mapper.CommentMapper.dtoToComment;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public CommentGetDto postComment(Long userId, Long eventId, CommentCreationDto commentCreationDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntityException("there is no such user with id=" + userId);
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundEntityException("there is no such event with id=" + eventId);
        }
        User user = userRepository.findById(userId).get();
        Event event = eventRepository.findById(eventId).get();
        Comment comment = commentRepository.save(dtoToComment(user, event, commentCreationDto));
        return commentToGetDto(comment);
    }

    public CommentGetDto patchComment(Long userId, Long eventId, CommentCreationDto commentCreationDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntityException("there is no such user with id=" + userId);
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundEntityException("there is no such event with id=" + eventId);
        }
        if (!commentRepository.existsById(commentCreationDto.getId())) {
            throw new NotFoundEntityException("there is no such comment with id=" + commentCreationDto.getId());
        }
        Comment comment = commentRepository.findById(commentCreationDto.getId()).get();
        if (!comment.getCommentator().getId().equals(userId)) {
            throw new NotFoundEntityException("there is no such comment with id=" + commentCreationDto.getId() + " from this user with id=" + userId);
        }
        if (!comment.getEvent().getId().equals(eventId)) {
            throw new NotFoundEntityException("there is no such comment with id=" + commentCreationDto.getId() + " for this event with id=" + eventId);
        }
        comment.setText(commentCreationDto.getText());
        return commentToGetDto(commentRepository.save(comment));
    }

    public void deleteComment(Long userId, Long eventId, Long commentId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntityException("there is no such user with id=" + userId);
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundEntityException("there is no such event with id=" + eventId);
        }
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundEntityException("there is no such comment with id=" + commentId);
        }
        Comment comment = commentRepository.findById(commentId).get();
        if (!comment.getCommentator().getId().equals(userId)) {
            throw new NotFoundEntityException("there is no such comment with id=" + commentId + " from this user with id=" + userId);
        }
        if (!comment.getEvent().getId().equals(eventId)) {
            throw new NotFoundEntityException("there is no such comment with id=" + commentId + " for this event with id=" + eventId);
        }
        commentRepository.deleteById(commentId);
    }

    public List<CommentGetDto> getUserComments(Long userId,
                                               Long eventId,
                                               String rangeStart,
                                               String rangeEnd,
                                               Integer from,
                                               Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        List<CommentGetDto> result = new ArrayList<>();
        if (rangeStart == null) {
            rangeStart = getDistantPastTimeString();
        }
        if (rangeEnd == null) {
            rangeEnd = getDistantFutureTimeString();
        }
        if (LocalDateTime.parse(rangeStart, formatter).isAfter(LocalDateTime.parse(rangeEnd, formatter))) {
            throw new WrongParamsException("wrong dates in filters");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntityException("there is no such user with id=" + userId);
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundEntityException("there is no such event with id=" + eventId);
        }
        User user = userRepository.findById(userId).get();
        Event event = eventRepository.findById(eventId).get();
        Page<Comment> commentPage = commentRepository.findAllByEventAndCommentatorAndCreatedIsAfterAndCreatedIsBefore(event, user, LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter), page);
        commentPage.getContent().forEach(comment -> {
            CommentGetDto commentGetDto = commentToGetDto(comment);
            result.add(commentGetDto);
        });

        if (result.size() > from % size) {
            return result.subList(from % size, result.size());
        } else return new ArrayList<>();
    }


    public CommentGetDto getCommentById(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundEntityException("there is no such comment with id=" + commentId);
        }
        return commentToGetDto(commentRepository.findById(commentId).get());
    }


    public List<CommentGetDto> getCommentsForEvent(
            Long eventId,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        List<CommentGetDto> result = new ArrayList<>();
        if (rangeStart == null) {
            rangeStart = getDistantPastTimeString();
        }
        if (rangeEnd == null) {
            rangeEnd = getDistantFutureTimeString();
        }
        if (LocalDateTime.parse(rangeStart, formatter).isAfter(LocalDateTime.parse(rangeEnd, formatter))) {
            throw new WrongParamsException("wrong dates in filters");
        }
        Event event = eventRepository.findById(eventId).get();
        Page<Comment> commentPage = commentRepository.findAllByEventAndCreatedIsAfterAndCreatedIsBefore(event, LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter), page);
        commentPage.getContent().forEach(comment -> {
            CommentGetDto commentGetDto = commentToGetDto(comment);
            result.add(commentGetDto);
        });

        if (result.size() > from % size) {
            return result.subList(from % size, result.size());
        } else return new ArrayList<>();
    }

}
