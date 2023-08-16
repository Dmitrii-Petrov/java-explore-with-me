package ru.practicum.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.StateAction;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.DateUtils.DATE_FORMAT;


@Data

@AllArgsConstructor
@NoArgsConstructor
public class EventCreationDto {


    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime createdOn;

    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    private User initiator;

    private Location location;

    private Boolean paid = false;

    private Long participantLimit = 0L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime publishedOn;

    private Boolean requestModeration = true;

    private State state;

    @Size(min = 3, max = 120)
    private String title;

    private StateAction stateAction;
}
