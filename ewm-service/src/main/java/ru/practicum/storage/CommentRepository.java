package ru.practicum.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import java.time.LocalDateTime;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByEventAndCommentatorAndCreatedIsAfterAndCreatedIsBefore(Event event, User user, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Comment> findAllByEventAndCreatedIsAfterAndCreatedIsBefore(Event event, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
