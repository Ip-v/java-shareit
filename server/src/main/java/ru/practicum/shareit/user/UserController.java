package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Контроллер пользователей
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("Создание пользователя {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable Long userId) {
        log.info("Запрос данных пользователя id {}", userId);
        return userService.get(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Удаление пользователя с id {}", userId);
        userService.delete(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId, @RequestBody UserDto userDto) {
        userDto.setId(userId);
        log.info("Обновление данных пользователя id {} data {}", userId, userDto);
        return userService.update(userDto);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Запрос всех пользователей");
        return userService.getAll();
    }
}
