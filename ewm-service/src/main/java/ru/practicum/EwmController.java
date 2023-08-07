package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
@Validated
public class EwmController {

//    private final StatService statService;

//    @GetMapping("/stats")
//    public List<StatDTO> getStats(@RequestParam String start,
//                                  @RequestParam String end,
//                                  @RequestParam(required = false) List<String> uris,
//                                  @RequestParam(required = false, defaultValue = "false") Boolean unique
//    ) {
//        log.info("поулчен запрос GET /stats");
//        return statService.getStats(start, end, uris, unique);
//    }
//
//    @PostMapping("/hit")
//    public void hitStat(@RequestBody StatHitDTO statHitDTO) {
//        log.info("поулчен запрос POST /hit");
//        statService.hitStat(statHitDTO);
//    }

}
