package ru.practicum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static LocalDateTime getErrorTime() {
        return LocalDateTime.now();
    }

    public static String getDistantFutureTimeString() {
        return LocalDateTime.now().plusYears(1000).format(formatter);
    }

    public static String getNowTimeString() {
        return LocalDateTime.now().format(formatter);
    }

    public static String getDistantPastTimeString() {
        return LocalDateTime.now().minusYears(1000).format(formatter);
    }

    public static LocalDateTime getEventStartMinimum() {
        return LocalDateTime.now().plusHours(2);
    }

}
