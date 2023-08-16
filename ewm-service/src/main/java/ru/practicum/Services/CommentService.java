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

//    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
//        Pageable page = PageRequest.of(from / size, size);
//        Page<User> userPage;
//        if (ids != null) {
//            userPage = userRepository.findByIdIn(ids, page);
//        } else {
//            userPage = userRepository.findAll(page);
//        }
//
//        List<UserDto> result = new ArrayList<>();
//        userPage.getContent().forEach(user -> {
//            result.add(userToDto(user));
//        });
//
//        if (result.size() > from % size) {
//            return result.subList(from % size, result.size());
//        } else return new ArrayList<>();
//
//    }
//
//    public UserDto saveUser(UserDto userDto) {
//        if (userRepository.existsByName(userDto.getName())) {
//            throw new FailNameException("there is already user with such name");
//        }
//
//        User user = userRepository.save(dtoToUser(userDto));
//        userDto.setId(user.getId());
//        return userDto;
//    }
//
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }

    public CommentGetDto postComment(Long userId, Long eventId, CommentCreationDto commentCreationDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntityException("there is no such user");
        }

        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundEntityException("there is no such event");
        }

        User user = userRepository.findById(userId).get();
        Event event = eventRepository.findById(eventId).get();
        Comment comment = commentRepository.save(dtoToComment(user, event, commentCreationDto));

        return commentToGetDto(comment);
    }

    public CommentGetDto patchComment(Long userId, Long eventId, CommentCreationDto commentCreationDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntityException("there is no such user");
        }

        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundEntityException("there is no such event");
        }

        if (!commentRepository.existsById(commentCreationDto.getId())) {
            throw new NotFoundEntityException("there is no such comment");
        }

        Comment comment = commentRepository.findById(commentCreationDto.getId()).get();

        if (!comment.getCommentator().getId().equals(userId)) {
            throw new NotFoundEntityException("there is no such comment from this user");
        }

        if (!comment.getEvent().getId().equals(eventId)) {
            throw new NotFoundEntityException("there is no such comment for this event");
        }

        comment.setText(commentCreationDto.getText());

        return commentToGetDto(commentRepository.save(comment));
    }

    public void deleteComment(Long userId, Long eventId, Long commentId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntityException("there is no such user");
        }

        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundEntityException("there is no such event");
        }

        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundEntityException("there is no such comment");
        }

        Comment comment = commentRepository.findById(commentId).get();

        if (!comment.getCommentator().getId().equals(userId)) {
            throw new NotFoundEntityException("there is no such comment from this user");
        }

        if (!comment.getEvent().getId().equals(eventId)) {
            throw new NotFoundEntityException("there is no such comment for this event");
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
            throw new NotFoundEntityException("there is no such user");
        }

        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundEntityException("there is no such event");
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
            throw new NotFoundEntityException("there is no such comment");
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
