package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;
import ru.practicum.model.enums.Status;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Long countAllByEventIdAndStatus(Long id, Status status);


}
