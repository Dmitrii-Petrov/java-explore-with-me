package ru.practicum.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Services.CategoryService;
import ru.practicum.Services.EventService;
import ru.practicum.exceptions.NotFoundEntityException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(1000).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request.getRequestURI(), request.getRemoteAddr()));
    }


    @GetMapping("/events/{eventId}")
    public ResponseEntity<Object> getEventById(@PathVariable Long eventId,
                                               HttpServletRequest request) {
        log.info("Get event/{}", eventId);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getPublicEventById(eventId, request.getRequestURI(), request.getRemoteAddr()));
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

//private final ItemClient itemClient;
//
//    @GetMapping
//    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
//                                           @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
//                                           @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
//        log.info("Get items with userId={}, from={}, size={}", userId, from, size);
//        return itemClient.getItemsByUserId(userId, from, size);
//    }
//
//    @GetMapping("/{itemId}")
//    public ResponseEntity<Object> getItemById(@PathVariable @NotNull @PositiveOrZero Long itemId, @RequestHeader("X-Sharer-User-Id") @PositiveOrZero Long userId) {
//        log.info("Get with itemId ={}, with userId={}", itemId, userId);
//        return itemClient.getItemDtoByItemId(itemId, userId);
//    }
//
//
//    @PostMapping("/{itemId}/comment")
//    public ResponseEntity<Object> addComment(@PathVariable @NotNull Long itemId,
//                                             @RequestHeader("X-Sharer-User-Id") Long userId,
//                                             @RequestBody @Valid CommentDto commentDto) {
//        log.info("Post comment {} with itemId ={}, with userId={}", commentDto, itemId, userId);
//        return itemClient.addComment(itemId, userId, commentDto);
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<Object> getItemsByTextSearch(@RequestHeader("X-Sharer-User-Id") Long userId,
//                                                       @RequestParam String text,
//                                                       @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
//                                                       @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
//        log.info("Get /search with text={} , from={}, size={}", text, from, size);
//        return itemClient.getItemsByTextSearch(userId, text, from, size);
//    }
//
//    @PostMapping()
//    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
//        log.info("Post item {} with userId={}", itemDto, userId);
//        return itemClient.create(itemDto, userId);
//    }
//
//    @PatchMapping("/{itemId}")
//    public ResponseEntity<Object> update(@PathVariable @NotNull Long itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
//        log.info("Patch item {} with itemId ={}, with userId={}", itemDto, itemId, userId);
//        return itemClient.update(itemDto, itemId, userId);
//    }

}
