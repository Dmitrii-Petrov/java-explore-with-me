package ru.practicum.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.CategoryDto;
import ru.practicum.DTO.EventFullDto;
import ru.practicum.DTO.UserDto;
import ru.practicum.Services.CategoryService;
import ru.practicum.Services.EventService;
import ru.practicum.Services.UserService;
import ru.practicum.exceptions.DuplicateNameException;
import ru.practicum.exceptions.FailCategoryNameException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final CategoryService categoryService;
    private final UserService userService;

    private final EventService eventService;


    @PostMapping("/categories")
    public ResponseEntity<Object> postCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Post category {} ", categoryDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.postCategory(categoryDto));
    }


    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable @NotNull @PositiveOrZero Long catId) {
        log.info("Delete category with Id = {} ", catId);
        categoryService.deleteCategory(catId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<Object> patchCategory(@PathVariable @NotNull @PositiveOrZero Long catId, @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Patch category with Id = {}, body = {}", catId, categoryDto);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(categoryService.patchCategory(catId, categoryDto));
    }

    @ExceptionHandler(value = {FailCategoryNameException.class})
    public ResponseEntity<Object> handleUnknownStateException(final FailCategoryNameException ex) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", HttpStatus.BAD_REQUEST.name());
        response.put("reason", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {DuplicateNameException.class})
    public ResponseEntity<Object> handleUnknownStateException(final DuplicateNameException ex) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", HttpStatus.CONFLICT.name());
        response.put("reason", HttpStatus.CONFLICT.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }


    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(required = false, defaultValue = "0") Integer from,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get users {}, from = {}, size ={}", ids, from, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUsers(ids, from, size));
    }

    @PostMapping("/users")
    public ResponseEntity<Object> postUser(@RequestBody @Valid UserDto userDto) {
        log.info("Post users {}", userDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.saveUser(userDto));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable @NotNull @PositiveOrZero Long userId) {
        log.info("Delete user by id {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }


    @PatchMapping("/events/{eventId}")
    public ResponseEntity<Object> patchEvent(@PathVariable @NotNull @PositiveOrZero Long eventId, @RequestBody @Valid EventFullDto eventFullDto) {
        log.info("Patch events with eventId = {}, body = {}", eventId, eventFullDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchEventByAdmin(eventId, eventFullDto));
    }


    @GetMapping("/events")
    public ResponseEntity<Object> getEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                   @RequestParam(required = false) List<String> states,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false) String rangeStart,
                                                   @RequestParam(required = false) String rangeEnd,
                                                   @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Get events with users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(1000).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size));
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
