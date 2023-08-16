package ru.practicum.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.User;
import ru.practicum.model.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.DateUtils.DATE_FORMAT;


@Data

@AllArgsConstructor
@NoArgsConstructor

public class RequestDto {


    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime created;

    private Long event;

    private User requester;

    private Status status;

    private List<Long> requestIds;


}
