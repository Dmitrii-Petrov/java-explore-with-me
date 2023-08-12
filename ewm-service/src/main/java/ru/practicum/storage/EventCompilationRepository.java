package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.EventCompilation;

import java.util.List;

@Repository
public interface EventCompilationRepository extends JpaRepository<EventCompilation, Long> {

    void deleteAllByCompilation(Compilation compilation);

    void deleteAllByEventNotIn(List<Event> list);

    List<EventCompilation> findAllByEventIn(List<Event> list);

    List<EventCompilation> findAllByCompilation(Compilation compilation);
}
