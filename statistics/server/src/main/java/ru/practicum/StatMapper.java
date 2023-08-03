package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StatMapper {

    public static Stat mapToNewStat(StatHitDTO statHitDTO) {
        Stat stat = new Stat();
        stat.setApp(statHitDTO.getApp());
        stat.setIp(statHitDTO.getIp());
        stat.setUri(statHitDTO.getIp());
        stat.setTimestamp(statHitDTO.getTimestamp());
        return stat;
    }
}
