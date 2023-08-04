package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class StatHitDTO {

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
