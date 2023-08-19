package ru.practicum.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.DateUtils.DATE_FORMAT;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentGetDto {


    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime created;

    private Long event;

    private Long commentator;

    @NotNull
    @NotBlank
    @Size(max = 7000, min = 1)
    private String text;
}
