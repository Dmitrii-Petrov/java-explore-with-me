package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.DTO.RequestDto;
import ru.practicum.model.Request;

@Component
@AllArgsConstructor
public class RequestMapper {

    public static RequestDto requestToDto(Request request) {
        return new RequestDto(request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester(),
                request.getStatus(),
                null);
    }


//    public static Request dtoToRequest(RequestDto requestDto) {
//        Request request = new Request();
//        request.setId(requestDto.getId());
//        request.setCreated(requestDto.getCreated());
//        request.setEvent(requestDto.getEvent());
//        request.setDescription(requestDto.getDescription());
//        request.setRequester(requestDto.getRequester());
//        request.setStatus(requestDto.getStatus());
//
//        return request;
//    }

}
