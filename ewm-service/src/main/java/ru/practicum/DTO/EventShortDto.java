package ru.practicum.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;


@Data

@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private Long id;

    private String annotation;

    private Long category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Boolean paid;

    @Enumerated(EnumType.STRING)
    private String title;

    private Long views;

    private Long confirmedRequests;
}
