package ru.practicum.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.DTO.*;
import ru.practicum.Services.CategoryService;
import ru.practicum.Services.CompilationService;
import ru.practicum.Services.EventService;
import ru.practicum.Services.UserService;
import ru.practicum.exceptions.BadEntityException;
import ru.practicum.exceptions.FailNameException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.DateUtils.*;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> postCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Post category {} ", categoryDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.postCategory(categoryDto));
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable @NotNull @PositiveOrZero Long catId) {
        log.info("Delete category with Id = {} ", catId);
        categoryService.deleteCategory(catId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> patchCategory(@PathVariable @NotNull @PositiveOrZero Long catId, @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Patch category with Id = {}, body = {}", catId, categoryDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.patchCategory(catId, categoryDto));
    }



    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventFullDto> patchEvent(@PathVariable @NotNull @PositiveOrZero Long eventId, @RequestBody @Valid EventFullDto eventFullDto) {
        log.info("Patch events with eventId = {}, body = {}", eventId, eventFullDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.patchEventByAdmin(eventId, eventFullDto));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventFullDto>> getEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                               @RequestParam(required = false) List<String> states,
                                                               @RequestParam(required = false) List<Long> categories,
                                                               @RequestParam(required = false) String rangeStart,
                                                               @RequestParam(required = false) String rangeEnd,
                                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                               @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Get events with users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        if (rangeStart == null) {
            rangeStart = getNowTimeString();
        }
        if (rangeEnd == null) {
            rangeEnd = getDistantFutureTimeString();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) List<Long> ids,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get users {}, from = {}, size ={}", ids, from, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUsers(ids, from, size));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> postUser(@RequestBody @Valid UserDto userDto) {
        log.info("Post users {}", userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.saveUser(userDto));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable @NotNull @PositiveOrZero Long userId) {
        log.info("Delete user by id {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }


    @PostMapping("/compilations")
    public ResponseEntity<CompilationDto> postCompilation(@RequestBody @Valid CompilationNewDto compilationNewDto) {
        log.info("Post /compilations {}", compilationNewDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(compilationService.postCompilation(compilationNewDto));
    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<HttpStatus> deleteCompilation(@PathVariable Long compId) {
        log.info("Delete /compilations/{}", compId);
        compilationService.deleteCompilation(compId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> postCompilation(@PathVariable Long compId, @RequestBody @Valid CompilationPatchDto compilationNewDto) {
        log.info("Patch /compilations/{} with {}", compId, compilationNewDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(compilationService.patchCompilation(compId, compilationNewDto));
    }
}
