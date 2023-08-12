package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static ru.practicum.DateUtils.formatter;


@Component
@AllArgsConstructor
public class StatMapper {

    public static Stat mapToNewStat(StatHitDTO statHitDTO) {
        Stat stat = new Stat();
        stat.setApp(statHitDTO.getApp());
        stat.setIp(statHitDTO.getIp());
        stat.setUri(statHitDTO.getUri());
        stat.setTimestamp(LocalDateTime.parse(
                statHitDTO.getTimestamp(),
                formatter));
        return stat;
    }
}
