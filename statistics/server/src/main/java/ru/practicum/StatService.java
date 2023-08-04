package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (!unique) {
            if (uris == null) {
                return statRepository.countHitsNotUniqueIpNullUris(LocalDateTime.parse(start, myFormatObj), LocalDateTime.parse(end, myFormatObj));
            } else
                return statRepository.countHitsNotUniqueIp(uris, LocalDateTime.parse(start, myFormatObj), LocalDateTime.parse(end, myFormatObj));
        } else if (uris == null) {
            return statRepository.countHitsUniqueIpNullUris(LocalDateTime.parse(start, myFormatObj), LocalDateTime.parse(end, myFormatObj));
        } else
            return statRepository.countHitsUniqueIp(uris, LocalDateTime.parse(start, myFormatObj), LocalDateTime.parse(end, myFormatObj));
    }
}
