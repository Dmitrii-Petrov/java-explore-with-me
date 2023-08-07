package ru.practicum;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
