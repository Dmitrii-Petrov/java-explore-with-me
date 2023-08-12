package ru.practicum.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private Long event;

    private Long requester;

    private Status status;
}
