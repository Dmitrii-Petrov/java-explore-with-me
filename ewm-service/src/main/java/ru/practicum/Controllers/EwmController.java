package ru.practicum.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.CategoryDto;
import ru.practicum.Services.CategoryService;
import ru.practicum.exceptions.DuplicateNameException;
import ru.practicum.exceptions.FailCategoryNameException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EwmController {

    private final CategoryService categoryService;


    @PostMapping("/categories")
    public ResponseEntity<Object> postCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Post category {} ", categoryDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.postCategory(categoryDto));
    }

    @ExceptionHandler(value = {FailCategoryNameException.class})
    public ResponseEntity<Object> handleUnknownStateException(final FailCategoryNameException ex) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", HttpStatus.BAD_REQUEST.name());
        response.put("reason", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("message",ex.getMessage());
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
        response.put("message",ex.getMessage());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
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
