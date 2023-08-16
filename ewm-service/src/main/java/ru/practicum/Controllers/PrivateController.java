package ru.practicum.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.*;
import ru.practicum.Services.EventService;
import ru.practicum.exceptions.BadEntityException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.DateUtils.getErrorTime;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventFullDto>> getEvents(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                        @RequestParam(name = "size", defaultValue = "10") @Positive Integer size
    ) {
        log.info("Get users/{}/events with from={}, size={}", userId, from, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getEvents(userId, from, size));
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventCreationAnswerDto> postEvent(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                            @RequestBody @Valid EventCreationDto eventCreationDto) {
        log.info("Post users/{}/events with {}", userId, eventCreationDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.postEvent(eventCreationDto, userId));
    }


    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventByUser(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                       @PathVariable @NotNull @PositiveOrZero Long eventId) {
        log.info("Get users/{}/events/{}", userId, eventId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getEventByUser(userId, eventId));
    }


    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventCreationAnswerDto> patchEventByUser(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                                   @PathVariable @NotNull @PositiveOrZero Long eventId,
                                                                   @RequestBody @Valid EventFullDto eventFullDto) {
        log.info("Patch users/{}/events/{} with {}", userId, eventId, eventFullDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchEventByUser(userId, eventId, eventFullDto));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<RequestShortDto>> getRequestsByUser(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                                   @PathVariable @NotNull @PositiveOrZero Long eventId) {
        log.info("Get users/{}/events/{}/requests", userId, eventId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getRequestsByUser(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestUpdateDto> patchRequestByUser(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                               @PathVariable @NotNull @PositiveOrZero Long eventId,
                                                               @RequestBody RequestDto requestDto) {
        log.info("Patch users/{}/events/{}/requests with {}", userId, eventId, requestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchRequest(userId, eventId, requestDto));
    }


    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<RequestShortDto>> getRequests(@PathVariable @NotNull @PositiveOrZero Long userId) {
        log.info("Post users/{}/requests", userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getRequests(userId));
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<RequestShortDto> postRequest(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                       @RequestParam @NotNull @PositiveOrZero Long eventId) {
        log.info("Post users/{}/requests with eventId={}", userId, eventId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.postRequest(userId, eventId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestShortDto> cancelRequest(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                         @PathVariable @NotNull @PositiveOrZero Long requestId) {
        log.info("Post users/{}/requests/{}/cancel", userId, requestId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.cancelRequest(userId, requestId));
    }

    @ExceptionHandler(value = {BadEntityException.class})
    public ResponseEntity<Map<String, Object>> handleUnknownStateException(final BadEntityException ex) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", HttpStatus.CONFLICT.name());
        response.put("reason", HttpStatus.CONFLICT.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("timestamp", getErrorTime());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

}
