package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.DateUtils.formatter;
import static ru.practicum.StatMapper.mapToNewStat;

@Service
@RequiredArgsConstructor
@Transactional
public class StatService {

    private final StatRepository statRepository;

    public void hitStat(StatHitDTO statHitDTO) {
        statRepository.save(mapToNewStat(statHitDTO));
    }

    public List<StatDTO> getStats(String start, String end, List<String> uris, Boolean unique) {
        List<StatDTO> result;
        if (!unique) {
            if (uris == null) {
                result = statRepository.countHitsNotUniqueIpNullUris(LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));
            } else
                result = statRepository.countHitsNotUniqueIp(uris, LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));
        } else if (uris == null) {
            result = statRepository.countHitsUniqueIpNullUris(LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));
        } else
            result = statRepository.countHitsUniqueIp(uris, LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));
        return result;
    }
}
