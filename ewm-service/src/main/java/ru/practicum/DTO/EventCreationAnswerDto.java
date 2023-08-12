package ru.practicum.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Category;
import ru.practicum.model.User;
import ru.practicum.model.enums.State;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Data

@AllArgsConstructor
@NoArgsConstructor
public class EventCreationAnswerDto {


    private Long id;

    @Size(min = 20, max = 2000)
    private String annotation;

    private Category category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @Size(min = 20, max = 7000)
    private String description;

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private User initiator;

    private LocationDto location;

    private Boolean paid;

    private Long participantLimit;


    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    @Size(min = 3, max = 120)
    private String title;

    private Long views;

    private Long confirmedRequests;
}