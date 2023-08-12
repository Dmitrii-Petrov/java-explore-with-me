package ru.practicum.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.EventCreationDto;
import ru.practicum.DTO.EventFullDto;
import ru.practicum.DTO.RequestDto;
import ru.practicum.Services.EventService;
import ru.practicum.exceptions.BadEntityException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<Object> getEvents(@PathVariable @NotNull @PositiveOrZero Long userId,
                                            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size
    ) {
        log.info("Get users/{}/events with from={}, size={}", userId, from, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getEvents(userId, from, size));
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<Object> postEvent(@PathVariable @NotNull @PositiveOrZero Long userId,
                                            @RequestBody @Valid EventCreationDto eventCreationDto) {
        log.info("Post users/{}/events with {}", userId, eventCreationDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.postEvent(eventCreationDto, userId));
    }


    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> getEventByUser(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                 @PathVariable @NotNull @PositiveOrZero Long eventId) {
        log.info("Get users/{}/events/{}", userId, eventId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getEventByUser(userId, eventId));
    }


    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> patchEventByUser(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                   @PathVariable @NotNull @PositiveOrZero Long eventId,
                                                   @RequestBody @Valid EventFullDto eventFullDto) {
        log.info("Patch users/{}/events/{} with {}", userId, eventId, eventFullDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchEventByUser(userId, eventId, eventFullDto));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> getRequestsByUser(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                    @PathVariable @NotNull @PositiveOrZero Long eventId) {
        log.info("Get users/{}/events/{}/requests", userId, eventId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getRequestsByUser(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> patchRequestByUser(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                     @PathVariable @NotNull @PositiveOrZero Long eventId,
                                                     @RequestBody RequestDto requestDto) {
        log.info("Patch users/{}/events/{}/requests with {}", userId, eventId, requestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchRequest(userId, eventId, requestDto));
    }


    @GetMapping("/{userId}/requests")
    public ResponseEntity<Object> getRequests(@PathVariable @NotNull @PositiveOrZero Long userId) {
        log.info("Post users/{}/requests", userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getRequests(userId));
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<Object> postRequest(@PathVariable @NotNull @PositiveOrZero Long userId,
                                              @RequestParam @NotNull @PositiveOrZero Long eventId) {
        log.info("Post users/{}/requests with eventId={}", userId, eventId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.postRequest(userId, eventId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                @PathVariable @NotNull @PositiveOrZero Long requestId) {
        log.info("Post users/{}/requests/{}/cancel", userId, requestId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.cancelRequest(userId, requestId));
    }

    @ExceptionHandler(value = {BadEntityException.class})
    public ResponseEntity<Object> handleUnknownStateException(final BadEntityException ex) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", HttpStatus.CONFLICT.name());
        response.put("reason", HttpStatus.CONFLICT.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

}
