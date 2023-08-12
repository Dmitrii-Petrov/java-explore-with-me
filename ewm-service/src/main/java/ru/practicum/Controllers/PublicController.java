package ru.practicum.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Services.CategoryService;
import ru.practicum.Services.CompilationService;
import ru.practicum.Services.EventService;
import ru.practicum.exceptions.NotFoundEntityException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicController {

    private final CategoryService categoryService;

    private final EventService eventService;

    private final CompilationService compilationService;


    @GetMapping("/events")
    public ResponseEntity<Object> getEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            //EVENT_DATE, VIEWS
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                            HttpServletRequest request) {
        log.info("Get events with text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request.getRemoteAddr(), request.getRequestURI()));
    }


    @GetMapping("/events/{eventId}")
    public ResponseEntity<Object> getEventById(@PathVariable Long eventId,
                                               HttpServletRequest request) {
        log.info("Get event/{}", eventId);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getPublicEventById(eventId, request.getRemoteAddr(), request.getRequestURI()));
    }


    @ExceptionHandler(value = {NotFoundEntityException.class})
    public ResponseEntity<Object> handleUnknownStateException(final NotFoundEntityException ex) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", HttpStatus.NOT_FOUND.name());
        response.put("reason", HttpStatus.NOT_FOUND.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    @GetMapping("/categories")
    public ResponseEntity<Object> getCategories(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Get categories with  from={}, size={}", from, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategories(from, size));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<Object> getCategory(@PathVariable Long catId) {
        log.info("Get categories/{}", catId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategory(catId));
    }

    @GetMapping("/compilations")
    public ResponseEntity<Object> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                  @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Get compilations with pinned={}  from={}, size={}", pinned, from, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(compilationService.getCompilations(pinned, from, size));
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<Object> getCompilationsById(@PathVariable Long compId) {
        log.info("Get compilations/{}", compId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(compilationService.getCompilationById(compId));
    }

}