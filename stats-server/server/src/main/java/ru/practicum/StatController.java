package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.DateUtils.formatter;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
@Validated
public class StatController {

    private final StatService statService;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(required = false) String start,
                                           @RequestParam(required = false) String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("поулчен запрос GET /stats with start={}, end={},uris={},unique={}", start, end, uris, unique);

        if (start == null) {
            start = LocalDateTime.now().minusYears(1000).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (end == null) {
            end = LocalDateTime.now().plusYears(1000).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }


        if (LocalDateTime.parse(start, formatter).isAfter(LocalDateTime.parse(end, formatter))) {
            Map<String, Object> response = new LinkedHashMap<>();

            response.put("status", HttpStatus.NOT_FOUND.name());
            response.put("reason", HttpStatus.NOT_FOUND.getReasonPhrase());
            response.put("message", "Неверно указаны даты");
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        } else


            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(statService.getStats(start, end, uris, unique));
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> hitStat(@RequestBody StatHitDTO statHitDTO) {
        log.info("поулчен запрос POST /hit");
        statService.hitStat(statHitDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(null);
    }

}
