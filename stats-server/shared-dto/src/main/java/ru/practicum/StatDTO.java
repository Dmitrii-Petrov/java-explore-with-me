package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatDTO {

    private String app;

    private String uri;

    private Long hits;
}
