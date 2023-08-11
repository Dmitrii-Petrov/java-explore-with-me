package ru.practicum.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.CompilationDto;
import ru.practicum.DTO.NewCompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.EventCompilation;
import ru.practicum.storage.CompilationRepository;
import ru.practicum.storage.EventCompilationRepository;
import ru.practicum.storage.EventRepository;

import java.util.List;

import static ru.practicum.mapper.CompilationMapper.dtoToCompilation;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventCompilationRepository eventCompilationRepository;

    private final EventRepository eventRepository;

    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = dtoToCompilation(newCompilationDto);
        Compilation updCompilation = compilationRepository.save(compilation);

        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(updCompilation.getId());
        compilationDto.setPinned(updCompilation.getPinned());
        compilationDto.setTitle(updCompilation.getTitle());

        newCompilationDto.getEvents().forEach(eventId -> {
            EventCompilation eventCompilation = new EventCompilation();
            eventCompilation.setCompilation(updCompilation);
            Event event = eventRepository.findById(eventId).get();
            eventCompilation.setEvent(event);
            compilationDto.getEvents().add(event);

            eventCompilationRepository.save(eventCompilation);
        });

        compilationRepository.save(compilation);

        return compilationDto;
    }


    public void deleteCompilation(Long id) {
        Compilation compilation = compilationRepository.findById(id).get();

        eventCompilationRepository.deleteAllByCompilation(compilation);

        compilationRepository.deleteById(id);
    }

    public CompilationDto patchCompilation(Long compId, NewCompilationDto newCompilationDto) {

        Compilation compilation = compilationRepository.findById(compId).get();

        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        compilationRepository.save(compilation);

        List<Event> eventList = eventRepository.findAllById(newCompilationDto.getEvents());
        eventCompilationRepository.deleteAllByEventNotIn(eventList);

        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());

        List<EventCompilation> eventCompilationList = eventCompilationRepository.findAllByEventIn(eventList);

        eventCompilationList.forEach(eventCompilation -> {
            compilationDto.getEvents().add(eventCompilation.getEvent());
        });

        return compilationDto;

    }

}
