package ru.practicum.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.enums.Status;

import java.time.LocalDateTime;


@Data

@AllArgsConstructor
@NoArgsConstructor

public class RequestShortDto {


    private Long id;

    private LocalDateTime created;

    private Long event;

    private Long requester;

    private Status status;
}
