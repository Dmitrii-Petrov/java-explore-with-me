package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.enums.Status;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Long countAllByEventIdAndStatus(Long id, Status status);

    List<Request> findAllByRequester(User user);

    List<Request> findAllByEvent(Event event);

    List<Request> findAllByEventAndRequester(Event event, User user);

    Long countAllByEventAndStatusIsNot(Event event, Status status);

    Long countAllByEventAndStatusIs(Event event, Status status);

}
