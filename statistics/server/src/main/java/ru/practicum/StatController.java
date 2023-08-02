package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Slf4j
@Validated
public class StatController {


//    @Autowired
//    public StatController(UserService userService) {
//        this.userService = userService;
//    }


    @GetMapping("/stats")
    public void getStats(@RequestParam(required = true) String start,
                         @RequestParam(required = true) String end,
                         @RequestParam List<String> uris,
                         @RequestParam Boolean unique
    ) {
        log.info("поулчен запрос GET /stats");
        return;
    }

    //
//    @GetMapping("/{userId}")
//    public Optional<User> getUsersById(@PathVariable(required = false) Long userId) {
//        log.info("поулчен запрос GET /users/id");
//        return userService.getUsersById(userId);
//    }
//
//
    @PostMapping("/hit")
    public void create() {
        log.info("поулчен запрос POST /hit");
    }
//
//    @PatchMapping("/{userId}")
//    public User update(@PathVariable Long userId, @RequestBody UserDto userDto) {
//        log.debug("поулчен запрос PATCH /users");
//        return userService.updateUser(userId, userDto);
//    }
//
//    @DeleteMapping("/{userId}")
//    public void delete(@PathVariable Long userId) {
//        log.debug("поулчен запрос DELETE /users");
//        userService.delete(userId);
//    }
}
