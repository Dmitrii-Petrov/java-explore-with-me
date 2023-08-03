package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatDTO {

    private String app;

    private String uri;

    private Long hits;
}
