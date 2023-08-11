package ru.practicum.Services;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.EventCreationDto;
import ru.practicum.DTO.EventFullDto;
import ru.practicum.DTO.EventShortDto;
import ru.practicum.DTO.RequestDto;
import ru.practicum.StatClient;
import ru.practicum.StatDTO;
import ru.practicum.exceptions.NotFoundEntityException;
import ru.practicum.model.Event;
import ru.practicum.model.QEvent;
import ru.practicum.model.Request;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.StateAction;
import ru.practicum.model.enums.Status;
import ru.practicum.storage.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.practicum.mapper.EventMapper.*;
import static ru.practicum.mapper.RequestMapper.requestToDto;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    private final StatClient statClient;

    public EventFullDto postEvent(EventCreationDto eventFullDto, Long userId) {
        locationRepository.save(eventFullDto.getLocation());
        eventFullDto.setCreatedOn(LocalDateTime.now());
        return eventToFullDto(eventRepository.save(dtoToEvent(eventFullDto, categoryRepository.findById(eventFullDto.getCategory()).get(), userRepository.findById(userId).get())));

    }


    public List<EventFullDto> getEvents(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        Page<Event> eventPage = eventRepository.findByInitiator(userRepository.findById(userId).get(), page);

        List<EventFullDto> result = new ArrayList<>();
        eventPage.getContent().forEach(event -> {
            result.add(eventToFullDto(event));
        });

        if (result.size() > from % size) {
            return result.subList(from % size, result.size());
        } else return new ArrayList<>();
    }

    public List<EventShortDto> getPublicEvents(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               String rangeStart,
                                               String rangeEnd,
                                               Boolean onlyAvailable,
                                               //EVENT_DATE, VIEWS
                                               String sort,
                                               Integer from,
                                               Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        List<EventShortDto> result = new ArrayList<>();
        BooleanExpression byPaid = QEvent.event.paid.eq(paid);
        BooleanExpression byCategoryIn = QEvent.event.category.id.in(categories);
        BooleanExpression byDescriptionSearch = QEvent.event.description.containsIgnoreCase(text);
        BooleanExpression byAnnotationSearch = QEvent.event.annotation.containsIgnoreCase(text);
        BooleanExpression byStartDate = QEvent.event.eventDate.after(LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        BooleanExpression byEndDate = QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        Page<Event> eventPage = eventRepository.findAll(byPaid.and(byCategoryIn.and(byDescriptionSearch.and(byAnnotationSearch.and(byStartDate.and(byEndDate))))), page);

        eventPage.getContent().forEach(event -> {
            result.add(eventToShortDto(event));
        });

        if (result.size() > from % size) {
            return result.subList(from % size, result.size());
        } else return new ArrayList<>();
    }


    public List<EventFullDto> getAdminEvents(List<Long> users,
                                              List<String> states,
                                              List<Long> categories,
                                              String rangeStart,
                                              String rangeEnd,
                                              Integer from,
                                              Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        List<EventFullDto> result = new ArrayList<>();
        BooleanExpression byUsers = QEvent.event.initiator.id.in(users);

        List<State> stateList = new ArrayList<>();
        if (states != null) {
            states.forEach(s -> stateList.add(State.valueOf(s)));
        }
        BooleanExpression byStates = QEvent.event.state.in(stateList);
        BooleanExpression byCategoryIn = QEvent.event.category.id.in(categories);

        BooleanExpression byStartDate = QEvent.event.eventDate.after(LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        BooleanExpression byEndDate = QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        Page<Event> eventPage = eventRepository.findAll(byUsers.and(byCategoryIn.and(byStates.and(byStartDate.and(byEndDate)))), page);

        Map<String, Object> parameters = Map.of("start", rangeStart, "end", rangeEnd);
        eventPage.getContent().forEach(event -> {

            EventFullDto eventFullDto = eventToFullDto(event);
            StatDTO[] statDTOList = statClient.getStatDTOs(parameters).getBody();

            if ((statDTOList != null)&&(statDTOList.length>0)) {
                eventFullDto.setViews(statDTOList[0].getHits());
            } else eventFullDto.setViews(0L);

            eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(event.getId(), Status.CONFIRMED));

            result.add(eventFullDto);
        });

        if (result.size() > from % size) {
            return result.subList(from % size, result.size());
        } else return new ArrayList<>();
    }


    public EventShortDto getPublicEventById(Long eventId) {
        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED);
        if (event == null) {
            throw new NotFoundEntityException("Event with id=" + eventId + " was not found");
        }
        EventShortDto eventShortDto = eventToShortDto(event);

        return eventShortDto;
    }


    public EventFullDto patchEventByAdmin(Long eventId, EventFullDto eventFullDto) {
        Event event = eventRepository.findById(eventId).get();
        if (eventFullDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventFullDto.getCategory()).get());
        }
        if (eventFullDto.getAnnotation() != null) {
            event.setAnnotation(eventFullDto.getAnnotation());
        }
        if (eventFullDto.getDescription() != null) {
            event.setDescription(eventFullDto.getDescription());
        }
        if (eventFullDto.getEventDate() != null) {
            event.setEventDate(eventFullDto.getEventDate());
        }
        if (eventFullDto.getLocation() != null) {
            event.setLocation(eventFullDto.getLocation());
        }
        if (eventFullDto.getPaid() != null) {
            event.setPaid(eventFullDto.getPaid());
        }
        if (eventFullDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventFullDto.getParticipantLimit());
        }
        if (eventFullDto.getRequestModeration() != null) {
            event.setRequestModeration(eventFullDto.getRequestModeration());
        }
        if ((eventFullDto.getStateAction() != null) && (eventFullDto.getStateAction() == StateAction.PUBLISH_EVENT)) {
            event.setState(State.PUBLISHED);
        }
        if ((eventFullDto.getStateAction() != null) && (eventFullDto.getStateAction() == StateAction.REJECT_EVENT)) {
            event.setState(State.CANCELED);
        }
        if (eventFullDto.getTitle() != null) {
            event.setTitle(eventFullDto.getTitle());
        }

        return eventToFullDto(eventRepository.save(event));
    }

    public EventFullDto patchEventByUser(Long userId, Long eventId, EventFullDto eventFullDto) {
        Event event = eventRepository.findById(eventId).get();
        if (eventFullDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventFullDto.getCategory()).get());
        }
        if (eventFullDto.getAnnotation() != null) {
            event.setAnnotation(eventFullDto.getAnnotation());
        }
        if (eventFullDto.getDescription() != null) {
            event.setDescription(eventFullDto.getDescription());
        }
        if (eventFullDto.getEventDate() != null) {
            event.setEventDate(eventFullDto.getEventDate());
        }
        if (eventFullDto.getLocation() != null) {
            event.setLocation(eventFullDto.getLocation());
        }
        if (eventFullDto.getPaid() != null) {
            event.setPaid(eventFullDto.getPaid());
        }
        if (eventFullDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventFullDto.getParticipantLimit());
        }
        if (eventFullDto.getRequestModeration() != null) {
            event.setRequestModeration(eventFullDto.getRequestModeration());
        }
        if ((eventFullDto.getStateAction() != null) && (eventFullDto.getStateAction() == StateAction.CANCEL_REVIEW)) {
            event.setState(State.CANCELED);
        }
        if ((eventFullDto.getStateAction() != null) && (eventFullDto.getStateAction() == StateAction.SEND_TO_REVIEW)) {
            event.setState(State.PENDING);
        }
        if (eventFullDto.getTitle() != null) {
            event.setTitle(eventFullDto.getTitle());
        }

        return eventToFullDto(eventRepository.save(event));
    }

    // TODO -правельный маппер
    //      -проверку на юзера
    public List<RequestDto> patchRequest(Long userId, Long eventId, RequestDto requestDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntityException("User with id=" + userId + " was not found");
        }

        Event event = eventRepository.findByIdAndInitiator(eventId,userRepository.findById(userId).get());
        if (event == null) {
            throw new NotFoundEntityException("Event with id=" + eventId + " was not found");
        }

        List<Request> requests = requestRepository.findAllById(requestDto.getRequestIds());
        List<RequestDto> result = new ArrayList<>();
        requests.forEach(request -> {
            request.setStatus(requestDto.getStatus());
            result.add(requestToDto(request));
        });
        requestRepository.saveAll(requests);

        return result;

    }

    public RequestDto postRequest(Long userId, Long eventId) {

        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(eventRepository.findById(eventId).get());
        request.setRequester(userRepository.findById(userId).get());
        request.setStatus(Status.PENDING);

        return requestToDto(requestRepository.save(request));
    }


//    public CategoryDto patchCategory(Long catId, CategoryDto categoryDto) {
//
//        Category category = dtoToCategory(categoryDto);
//        category.setId(catId);
//        return categoryToDto(categoryRepository.save(category));
//    }

}
