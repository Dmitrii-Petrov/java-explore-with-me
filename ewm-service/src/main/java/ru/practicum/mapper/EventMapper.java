package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.DTO.EventCreationAnswerDto;
import ru.practicum.DTO.EventCreationDto;
import ru.practicum.DTO.EventFullDto;
import ru.practicum.DTO.EventShortDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.model.enums.State;

import static ru.practicum.mapper.LocationMapper.locationToDto;
import static ru.practicum.mapper.UserMapper.userToShortDto;

@Component
@AllArgsConstructor
public class EventMapper {

    public static EventFullDto eventToFullDto(Event event) {
        return new EventFullDto(event.getId(),
                event.getAnnotation(),
                event.getCategory().getId(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getInitiator(),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                null,
                null,
                null);
    }

    public static EventCreationAnswerDto eventToCreationAnswerDto(Event event) {
        return new EventCreationAnswerDto(event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getInitiator(),
                locationToDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                0L,
                0L);
    }


    public static EventShortDto eventToShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(event.getCategory().getId());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setId(event.getId());
        eventShortDto.setInitiator(userToShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        return eventShortDto;
    }

    public static Event dtoToEvent(EventCreationDto eventCreationDto, Category category, User user) {
        Event event = new Event();
        event.setAnnotation(eventCreationDto.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(eventCreationDto.getCreatedOn());
        event.setDescription(eventCreationDto.getDescription());
        event.setEventDate(eventCreationDto.getEventDate());
        event.setInitiator(user);
        event.setLocation(eventCreationDto.getLocation());
        event.setPaid(eventCreationDto.getPaid());
        if (eventCreationDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventCreationDto.getParticipantLimit());
        } else event.setParticipantLimit(0L);
        event.setPublishedOn(eventCreationDto.getPublishedOn());
        event.setRequestModeration(eventCreationDto.getRequestModeration());
        if (eventCreationDto.getState() != null) {
            event.setState(eventCreationDto.getState());
        } else event.setState(State.PENDING);
        event.setTitle(eventCreationDto.getTitle());
        return event;
    }
}
