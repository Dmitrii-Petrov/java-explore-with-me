package ru.practicum.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.CompilationDto;
import ru.practicum.DTO.CompilationNewDto;
import ru.practicum.DTO.CompilationPatchDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.EventCompilation;
import ru.practicum.storage.CompilationRepository;
import ru.practicum.storage.EventCompilationRepository;
import ru.practicum.storage.EventRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.mapper.CompilationMapper.compilationToDto;
import static ru.practicum.mapper.CompilationMapper.dtoToCompilation;
import static ru.practicum.mapper.EventMapper.eventToShortDto;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventCompilationRepository eventCompilationRepository;

    private final EventRepository eventRepository;

    public CompilationDto postCompilation(CompilationNewDto compilationNewDto) {
        Compilation compilation = dtoToCompilation(compilationNewDto);
        Compilation updCompilation = compilationRepository.save(compilation);

        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(updCompilation.getId());
        compilationDto.setPinned(updCompilation.getPinned());
        compilationDto.setTitle(updCompilation.getTitle());

        compilationNewDto.getEvents().forEach(eventId -> {
            EventCompilation eventCompilation = new EventCompilation();
            eventCompilation.setCompilation(updCompilation);
            Event event = eventRepository.findById(eventId).get();
            eventCompilation.setEvent(event);
            compilationDto.getEvents().add(eventToShortDto(event));

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

    public CompilationDto patchCompilation(Long compId, CompilationPatchDto compilationNewDto) {

        Compilation compilation = compilationRepository.findById(compId).get();

        if (compilationNewDto.getTitle() != null) {
            compilation.setTitle(compilationNewDto.getTitle());
        }
        if (compilationNewDto.getPinned() != null) {
            compilation.setPinned(compilationNewDto.getPinned());
        }
        Compilation updCompilation = compilationRepository.save(compilation);

        List<Event> eventList = eventRepository.findAllById(compilationNewDto.getEvents());
        eventCompilationRepository.deleteAllByEventNotIn(eventList);

        CompilationDto compilationDto = new CompilationDto();

        compilationNewDto.getEvents().forEach(eventId -> {
            EventCompilation eventCompilation = new EventCompilation();
            eventCompilation.setCompilation(updCompilation);
            Event event = eventRepository.findById(eventId).get();
            eventCompilation.setEvent(event);
            compilationDto.getEvents().add(eventToShortDto(event));

            eventCompilationRepository.save(eventCompilation);
        });


        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());

        List<EventCompilation> eventCompilationList = eventCompilationRepository.findAllByEventIn(eventList);

        eventCompilationList.forEach(eventCompilation -> {
            compilationDto.getEvents().add(eventToShortDto(eventCompilation.getEvent()));
        });

        return compilationDto;

    }


    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<CompilationDto> compilationDtoList = new ArrayList<>();

        Pageable page = PageRequest.of(from / size, size);
        Page<Compilation> compilationPage;
        if (pinned == null) {
            compilationPage = compilationRepository.findAll(page);
        } else {
            compilationPage = compilationRepository.findAllByPinned(pinned, page);
        }


        compilationPage.getContent().forEach(compilation -> {
            CompilationDto compilationDto = new CompilationDto();
            compilationDto.setId(compilation.getId());
            compilationDto.setPinned(compilation.getPinned());
            compilationDto.setTitle(compilation.getTitle());
            compilationDto.setEvents(new ArrayList<>());
            List<EventCompilation> eventCompilationList = eventCompilationRepository.findAllByCompilation(compilation);
            eventCompilationList.forEach(eventCompilation -> {
                compilationDto.getEvents().add(eventToShortDto(eventCompilation.getEvent()));
            });
            compilationDtoList.add(compilationDto);
        });
        if (compilationDtoList.size() > from % size) {
            return compilationDtoList.subList(from % size, compilationDtoList.size());
        } else return new ArrayList<>();

    }


    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).get();

        CompilationDto compilationDto = compilationToDto(compilation);
        List<EventCompilation> eventCompilationList = eventCompilationRepository.findAllByCompilation(compilation);
        eventCompilationList.forEach(eventCompilation -> {
            compilationDto.getEvents().add(eventToShortDto(eventCompilation.getEvent()));
        });

        return compilationDto;

    }

}
