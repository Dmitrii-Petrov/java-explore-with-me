package ru.practicum.Services;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.*;
import ru.practicum.StatClient;
import ru.practicum.StatDTO;
import ru.practicum.StatHitDTO;
import ru.practicum.exceptions.BadEntityException;
import ru.practicum.exceptions.NotFoundEntityException;
import ru.practicum.exceptions.WrongParamsException;
import ru.practicum.model.Event;
import ru.practicum.model.QEvent;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.StateAction;
import ru.practicum.model.enums.Status;
import ru.practicum.storage.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ru.practicum.DateUtils.*;
import static ru.practicum.mapper.EventMapper.*;
import static ru.practicum.mapper.RequestMapper.requestToShortDto;

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

    public EventCreationAnswerDto postEvent(EventCreationDto eventCreationDto, Long userId) {
        if (eventCreationDto.getEventDate().isBefore(getEventStartMinimum())) {
            throw new BadEntityException("wrong event date");
        }
        locationRepository.save(eventCreationDto.getLocation());
        eventCreationDto.setCreatedOn(LocalDateTime.now());
        Event event = dtoToEvent(eventCreationDto, categoryRepository.findById(eventCreationDto.getCategory()).get(), userRepository.findById(userId).get());
        return eventToCreationAnswerDto(eventRepository.save(event));
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
                                               String sort,
                                               Integer from,
                                               Integer size,
                                               String ip,
                                               String uri) {
        Pageable page = PageRequest.of(from / size, size);
        List<EventShortDto> result = new ArrayList<>();
        if (rangeStart == null) {
            rangeStart = getNowTimeString();
        }
        if (rangeEnd == null) {
            rangeEnd = getDistantFutureTimeString();
        }
        if (LocalDateTime.parse(rangeStart, formatter).isAfter(LocalDateTime.parse(rangeEnd, formatter))) {
            throw new WrongParamsException("wrong dates in filters");
        }
        Page<Event> eventPage;
        BooleanExpression byStartDate = QEvent.event.eventDate.after(LocalDateTime.parse(rangeStart, formatter));
        BooleanExpression byEndDate = QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, formatter));

        if ((paid != null) && (categories != null) && (text != null)) {
            BooleanExpression byPaid = QEvent.event.paid.eq(paid);
            BooleanExpression byCategoryIn = QEvent.event.category.id.in(categories);
            BooleanExpression byDescriptionSearch = QEvent.event.description.containsIgnoreCase(text);
            BooleanExpression byAnnotationSearch = QEvent.event.annotation.containsIgnoreCase(text);
            eventPage = eventRepository.findAll(byPaid.and(byCategoryIn.and(byStartDate.and(byEndDate.and(byDescriptionSearch.or(byAnnotationSearch))))), page);
        } else if ((paid != null) && (categories != null)) {
            BooleanExpression byPaid = QEvent.event.paid.eq(paid);
            BooleanExpression byCategoryIn = QEvent.event.category.id.in(categories);
            eventPage = eventRepository.findAll(byPaid.and(byCategoryIn.and(byStartDate.and(byEndDate))), page);
        } else if ((paid != null) && (text != null)) {
            BooleanExpression byPaid = QEvent.event.paid.eq(paid);
            BooleanExpression byDescriptionSearch = QEvent.event.description.containsIgnoreCase(text);
            BooleanExpression byAnnotationSearch = QEvent.event.annotation.containsIgnoreCase(text);
            eventPage = eventRepository.findAll(byPaid.and(byStartDate.and(byEndDate.and(byDescriptionSearch.or(byAnnotationSearch)))), page);
        } else if ((categories != null) && (text != null)) {
            BooleanExpression byCategoryIn = QEvent.event.category.id.in(categories);
            BooleanExpression byDescriptionSearch = QEvent.event.description.containsIgnoreCase(text);
            BooleanExpression byAnnotationSearch = QEvent.event.annotation.containsIgnoreCase(text);
            eventPage = eventRepository.findAll(byCategoryIn.and(byStartDate.and(byEndDate.and(byDescriptionSearch.or(byAnnotationSearch)))), page);
        } else if (categories != null) {
            BooleanExpression byCategoryIn = QEvent.event.category.id.in(categories);
            eventPage = eventRepository.findAll(byCategoryIn.and(byStartDate.and(byEndDate)), page);
        } else if (paid != null) {
            BooleanExpression byPaid = QEvent.event.paid.eq(paid);
            eventPage = eventRepository.findAll(byPaid.and((byStartDate.and(byEndDate))), page);
        } else if (text != null) {
            BooleanExpression byDescriptionSearch = QEvent.event.description.containsIgnoreCase(text);
            BooleanExpression byAnnotationSearch = QEvent.event.annotation.containsIgnoreCase(text);
            eventPage = eventRepository.findAll(byStartDate.and(byEndDate.and(byDescriptionSearch.or(byAnnotationSearch))), page);
        } else eventPage = eventRepository.findAll(byEndDate.and(byStartDate), page);

        statClient.postHit(new StatHitDTO("ewm-service", uri, ip, getNowTimeString()));
        eventPage.getContent().forEach(event1 -> {
            EventShortDto eventShortDto = eventToShortDto(event1);
            result.add(eventShortDto);
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
        Page<Event> eventPage;
        BooleanExpression byStartDate = QEvent.event.eventDate.after(LocalDateTime.parse(rangeStart, formatter));
        BooleanExpression byEndDate = QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, formatter));
        if ((users != null) && (states != null) && (categories != null)) {
            BooleanExpression byUsers = QEvent.event.initiator.id.in(users);
            List<State> stateList = new ArrayList<>();
            states.forEach(s -> stateList.add(State.valueOf(s)));
            BooleanExpression byStates = QEvent.event.state.in(stateList);

            BooleanExpression byCategoryIn = QEvent.event.category.id.in(categories);
            eventPage = eventRepository.findAll(byUsers.and(byCategoryIn.and(byStates.and(byStartDate.and(byEndDate)))), page);
        } else if ((users != null) && (states != null)) {
            BooleanExpression byUsers = QEvent.event.initiator.id.in(users);
            List<State> stateList = new ArrayList<>();
            states.forEach(s -> stateList.add(State.valueOf(s)));
            BooleanExpression byStates = QEvent.event.state.in(stateList);
            eventPage = eventRepository.findAll(byUsers.and(byStates.and(byStartDate.and(byEndDate))), page);
        } else if ((users != null) && (categories != null)) {
            BooleanExpression byUsers = QEvent.event.initiator.id.in(users);
            BooleanExpression byCategoryIn = QEvent.event.category.id.in(categories);
            eventPage = eventRepository.findAll(byUsers.and(byCategoryIn.and(byStartDate.and(byEndDate))), page);
        } else if ((states != null) && (categories != null)) {
            List<State> stateList = new ArrayList<>();
            states.forEach(s -> stateList.add(State.valueOf(s)));
            BooleanExpression byStates = QEvent.event.state.in(stateList);
            BooleanExpression byCategoryIn = QEvent.event.category.id.in(categories);
            eventPage = eventRepository.findAll(byCategoryIn.and(byStates.and(byStartDate.and(byEndDate))), page);
        } else if (states != null) {
            List<State> stateList = new ArrayList<>();
            states.forEach(s -> stateList.add(State.valueOf(s)));
            BooleanExpression byStates = QEvent.event.state.in(stateList);
            eventPage = eventRepository.findAll(byStates.and(byStartDate.and(byEndDate)), page);
        } else if (categories != null) {
            BooleanExpression byCategoryIn = QEvent.event.category.id.in(categories);
            eventPage = eventRepository.findAll(byCategoryIn.and(byStartDate.and(byEndDate)), page);
        } else if (users != null) {
            BooleanExpression byUsers = QEvent.event.initiator.id.in(users);
            eventPage = eventRepository.findAll(byUsers.and(byStartDate.and(byEndDate)), page);
        } else eventPage = eventRepository.findAll(byStartDate.and(byEndDate), page);

        Map<String, Object> parameters = Map.of("start", rangeStart, "end", rangeEnd, "uris", new ArrayList<>(), "unique", false);
        eventPage.getContent().forEach(event -> {
            EventFullDto eventFullDto = eventToFullDto(event);
            StatDTO[] statDTOList = statClient.getStatDTOs(parameters).getBody();
            if ((statDTOList != null) && (statDTOList.length > 0)) {
                eventFullDto.setViews(statDTOList[0].getHits());
            } else eventFullDto.setViews(0L);
            eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(event.getId(), Status.CONFIRMED));
            result.add(eventFullDto);
        });
        if (result.size() > from % size) {
            return result.subList(from % size, result.size());
        } else return new ArrayList<>();
    }


    public EventFullDto getPublicEventById(Long eventId, String ip, String uri) {
        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED);
        if (event == null) {
            throw new NotFoundEntityException("Event with id=" + eventId + " was not found");
        }
        EventFullDto eventFullDto = eventToFullDto(event);
        statClient.postHit(new StatHitDTO("ewm-service", uri, ip, getNowTimeString()));
        StatDTO[] statDTOList = statClient.getStatDTOs(Map.of("start", getDistantPastTimeString(),
                "end", getDistantFutureTimeString(),
                "uris", uri,
                "unique", true)
        ).getBody();
        if ((statDTOList != null) && (statDTOList.length > 0)) {
            eventFullDto.setViews(statDTOList[0].getHits());
        } else eventFullDto.setViews(0L);
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(event.getId(), Status.CONFIRMED));
        return eventFullDto;
    }


    public EventFullDto patchEventByAdmin(Long eventId, EventFullDto eventFullDto) {
        Event event = eventRepository.findById(eventId).get();
        if ((event.getState() == State.PUBLISHED) && (eventFullDto.getStateAction() == StateAction.PUBLISH_EVENT)) {
            throw new BadEntityException("event with id=" + eventId + " is already published");
        }
        if ((event.getState() == State.CANCELED) && (eventFullDto.getStateAction() == StateAction.PUBLISH_EVENT)) {
            throw new BadEntityException("event with id=" + eventId + " is canceled");
        }
        if ((event.getState() == State.PUBLISHED) && (eventFullDto.getStateAction() == StateAction.REJECT_EVENT)) {
            throw new BadEntityException("event with id=" + eventId + " is already published");
        }
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
            if (!((Objects.equals(event.getLocation().getLat(), eventFullDto.getLocation().getLat())) && (Objects.equals(event.getLocation().getLon(), eventFullDto.getLocation().getLon())))) {
                event.setLocation(locationRepository.save(eventFullDto.getLocation()));
            }
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

    public EventFullDto getEventByUser(Long userId, Long eventId) {
        EventFullDto eventFullDto = eventToFullDto(eventRepository.findById(eventId).get());
        Map<String, Object> parameters = Map.of("start", getDistantPastTimeString(),
                "end", getDistantFutureTimeString(),
                "uris", "/event/" + eventId,
                "unique", true);

        StatDTO[] statDTOList = statClient.getStatDTOs(parameters).getBody();
        if ((statDTOList != null) && (statDTOList.length > 0)) {
            eventFullDto.setViews(statDTOList[0].getHits());
        } else eventFullDto.setViews(0L);
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, Status.CONFIRMED));
        return eventFullDto;
    }

    public EventCreationAnswerDto patchEventByUser(Long userId, Long eventId, EventFullDto eventFullDto) {
        Event event = eventRepository.findById(eventId).get();
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundEntityException("there is no such event with id=" + eventId);
        }
        if (event.getState() == State.PUBLISHED) {
            throw new BadEntityException("event with id=" + eventId + " is already published");
        }
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

        return eventToCreationAnswerDto(eventRepository.save(event));
    }


    public List<RequestShortDto> getRequestsByUser(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).get();
        List<RequestShortDto> result = new ArrayList<>();
        List<Request> requestList = requestRepository.findAllByEvent(event);
        requestList.forEach(request -> {
            result.add(requestToShortDto(request));
        });
        return result;
    }


    public RequestUpdateDto patchRequest(Long userId, Long eventId, RequestDto requestDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntityException("User with id=" + userId + " was not found");
        }
        Event event = eventRepository.findByIdAndInitiator(eventId, userRepository.findById(userId).get());
        if (event == null) {
            throw new NotFoundEntityException("Event with id=" + eventId + " was not found");
        }
        List<Request> requests = requestRepository.findAllById(requestDto.getRequestIds());
        RequestUpdateDto requestUpdateDto = new RequestUpdateDto();
        requests.forEach(request -> {
            if (request.getStatus() == Status.CONFIRMED) {
                throw new BadEntityException("request with id=" + request.getId() + " is already confirmed");
            }
            if ((event.getParticipantLimit() != 0) && (event.getParticipantLimit() <= requestRepository.countAllByEventAndStatusIs(event, Status.CONFIRMED))) {
                throw new BadEntityException("there is no room for more participants in event with id=" + eventId);
            }
            request.setStatus(requestDto.getStatus());

            if (request.getStatus() == Status.REJECTED) {
                requestUpdateDto.getRejectedRequests().add(new RequestShortDto(request.getId(), request.getCreated(), request.getEvent().getId(), request.getRequester().getId(), request.getStatus()));
            }
            if (request.getStatus() == Status.CONFIRMED) {

                requestUpdateDto.getConfirmedRequests().add(new RequestShortDto(request.getId(), request.getCreated(), request.getEvent().getId(), request.getRequester().getId(), request.getStatus()));
            }
        });
        requestRepository.saveAll(requests);
        return requestUpdateDto;
    }

    public List<RequestShortDto> getRequests(Long userId) {
        List<RequestShortDto> requestDtoList = new ArrayList<>();
        User user = userRepository.findById(userId).get();
        List<Request> requests = requestRepository.findAllByRequester(user);
        requests.forEach(request -> {
            requestDtoList.add(requestToShortDto(request));
        });
        return requestDtoList;
    }

    public RequestShortDto postRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).get();
        if (event.getInitiator().getId().equals(userId)) {
            throw new BadEntityException("request from event initiator");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new BadEntityException("event with id=" + eventId + " is not published yet");
        }
        if ((event.getParticipantLimit() != 0) && (event.getParticipantLimit() <= requestRepository.countAllByEventAndStatusIs(event, Status.CONFIRMED))) {
            throw new BadEntityException("there is no room for more participants for event with id=" + eventId);
        }
        if (!requestRepository.findAllByEventAndRequester(event, userRepository.findById(userId).get()).isEmpty()) {
            throw new BadEntityException("there is already such request for event with id=" + eventId + "from a user with id=" + userId);
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(userRepository.findById(userId).get());
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }
        return requestToShortDto(requestRepository.save(request));
    }

    public RequestShortDto cancelRequest(Long userId, Long eventId) {
        Request request = requestRepository.findById(eventId).get();
        request.setStatus(Status.CANCELED);
        return requestToShortDto(requestRepository.save(request));
    }
}
