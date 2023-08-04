package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class StatMapper {

    public static Stat mapToNewStat(StatHitDTO statHitDTO) {
        Stat stat = new Stat();
        stat.setApp(statHitDTO.getApp());
        stat.setIp(statHitDTO.getIp());
        stat.setUri(statHitDTO.getUri());
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        stat.setTimestamp(LocalDateTime.parse(
                statHitDTO.getTimestamp(),
                myFormatObj));
        return stat;
    }
}
