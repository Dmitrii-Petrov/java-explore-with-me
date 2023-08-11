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
import ru.practicum.Services.CategoryService;
import ru.practicum.Services.EventService;
import ru.practicum.Services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateController {

    private final CategoryService categoryService;
    private final UserService userService;
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
                .body(eventService.postEvent(eventCreationDto,userId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> patchEvent(@PathVariable @NotNull @PositiveOrZero Long userId,
                                             @PathVariable @NotNull @PositiveOrZero Long eventId,
                                             @RequestBody @Valid EventFullDto eventFullDto) {
        log.info("Patch users/{}/events/{} with {}", userId, eventId, eventFullDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchEventByUser(userId, eventId, eventFullDto));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> patchRequest(@PathVariable @NotNull @PositiveOrZero Long userId,
                                               @PathVariable @NotNull @PositiveOrZero Long eventId,
                                               @RequestBody RequestDto requestDto) {
        log.info("Patch users/{}/events/{}/requests with {}", userId, eventId, requestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchRequest(userId, eventId, requestDto));
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<Object> postRequest(@PathVariable @NotNull @PositiveOrZero Long userId,
                                              @RequestParam @NotNull @PositiveOrZero Long eventId) {
        log.info("Post users/{}/requests with eventId={}", userId, eventId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.postRequest(userId, eventId));
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
