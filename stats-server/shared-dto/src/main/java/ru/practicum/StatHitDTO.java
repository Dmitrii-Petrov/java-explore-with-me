package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class StatHitDTO {

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
