package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.DTO.EventCreationDto;
import ru.practicum.DTO.EventFullDto;
import ru.practicum.DTO.EventShortDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.model.enums.State;

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


    public static EventShortDto eventToShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(event.getCategory());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setId(event.getId());
        eventShortDto.setInitiator(event.getInitiator());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setDescription(event.getDescription());
        return eventShortDto;
    }

    public static Event dtoToEvent(EventFullDto eventFullDto, Category category, User user) {
        Event event = new Event();

        event.setAnnotation(eventFullDto.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(eventFullDto.getCreatedOn());
        event.setDescription(eventFullDto.getDescription());
        event.setEventDate(eventFullDto.getEventDate());
        event.setInitiator(user);
        event.setLocation(eventFullDto.getLocation());
        event.setPaid(eventFullDto.getPaid());

        if (eventFullDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventFullDto.getParticipantLimit());
        } else event.setParticipantLimit(0L);

        event.setPublishedOn(eventFullDto.getPublishedOn());
        event.setRequestModeration(eventFullDto.getRequestModeration());

        if (eventFullDto.getState() != null) {
            event.setState(eventFullDto.getState());
        } else event.setState(State.PENDING);

        event.setTitle(eventFullDto.getTitle());

        return event;
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
