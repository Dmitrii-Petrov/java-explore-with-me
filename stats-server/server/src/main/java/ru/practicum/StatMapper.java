package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@AllArgsConstructor
public class StatMapper {

    private static final DateUtils dateUtils = new DateUtils();

    public static Stat mapToNewStat(StatHitDTO statHitDTO) {
        Stat stat = new Stat();
        stat.setApp(statHitDTO.getApp());
        stat.setIp(statHitDTO.getIp());
        stat.setUri(statHitDTO.getUri());
        stat.setTimestamp(LocalDateTime.parse(
                statHitDTO.getTimestamp(),
                dateUtils.myFormatObj));
        return stat;
    }
}
