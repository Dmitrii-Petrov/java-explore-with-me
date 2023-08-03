package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@Validated
public class StatController {

    StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }


    @GetMapping("/stats")
    public List<StatDTO> getStats(@RequestParam(required = true) String start,
                                  @RequestParam(required = true) String end,
                                  @RequestParam List<String> uris,
                                  @RequestParam(required = false, defaultValue = "false") Boolean unique
    ) {
        log.info("поулчен запрос GET /stats");
        return statService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public void hitStat(StatHitDTO statHitDTO) {
        log.info("поулчен запрос POST /hit");
        statService.hitStat(statHitDTO);

    }
}
