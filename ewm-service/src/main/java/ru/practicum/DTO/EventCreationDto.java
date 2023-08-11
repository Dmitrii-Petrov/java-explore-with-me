package ru.practicum.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.StateAction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data

@AllArgsConstructor
@NoArgsConstructor
public class EventCreationDto {


    private Long id;

    @NotNull
    @NotBlank
    private String annotation;

    private Long category;


    private LocalDateTime createdOn;

    @NotNull
    @NotBlank
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private User initiator;

    private Location location;

    private Boolean paid;

    private Long participantLimit;


    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    private String title;

    private StateAction stateAction;
}
