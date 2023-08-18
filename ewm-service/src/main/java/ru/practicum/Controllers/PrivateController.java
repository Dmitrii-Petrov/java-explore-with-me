package ru.practicum.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.*;
import ru.practicum.Services.CommentService;
import ru.practicum.Services.EventService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateController {
    private final EventService eventService;
    private final CommentService commentService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventFullDto>> getEvents(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                        @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
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

    @PostMapping("/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentGetDto> postComment(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                     @PathVariable @NotNull @PositiveOrZero Long eventId,
                                                     @RequestBody @NotNull @Valid CommentCreationDto commentCreationDto) {
        log.info("Post /{}/events/{}/comments with {}", userId, eventId, commentCreationDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.postComment(userId, eventId, commentCreationDto));
    }

    @PatchMapping("/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentGetDto> patchComment(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                      @PathVariable @NotNull @PositiveOrZero Long eventId,
                                                      @RequestBody @NotNull @Valid CommentCreationDto commentCreationDto) {
        log.info("Patch /{}/events/{}/comments with {}", userId, eventId, commentCreationDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.patchComment(userId, eventId, commentCreationDto));
    }

    @DeleteMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> patchComment(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                   @PathVariable @NotNull @PositiveOrZero Long eventId,
                                                   @PathVariable @NotNull @PositiveOrZero Long commentId) {
        log.info("Delete /{}/events/{}/comments/{}", userId, eventId, commentId);
        commentService.deleteComment(userId, eventId, commentId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @GetMapping("/{userId}/events/{eventId}/comments")
    public ResponseEntity<List<CommentGetDto>> getUserComments(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                               @PathVariable @NotNull @PositiveOrZero Long eventId,
                                                               @RequestParam(required = false) String rangeStart,
                                                               @RequestParam(required = false) String rangeEnd,
                                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                               @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Get /{}/events/{}/comments with rangeStart={}, rangeEnd={}, from={}, size={}",
                userId, eventId, rangeStart, rangeEnd, from, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.getUserComments(userId, eventId, rangeStart, rangeEnd, from, size));
    }
}
