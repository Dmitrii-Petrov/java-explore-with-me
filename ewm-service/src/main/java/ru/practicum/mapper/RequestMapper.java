package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.DTO.RequestShortDto;
import ru.practicum.model.Request;

@Component
@AllArgsConstructor
public class RequestMapper {

    public static RequestShortDto requestToShortDto(Request request) {
        return new RequestShortDto(request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }
}
