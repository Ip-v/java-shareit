package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utils.Create;

/**
 * Контроллер пользователей
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("Создание пользователя {}", userDto);
        return client.create(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable Long userId) {
        log.info("Запрос данных пользователя id {}", userId);
        return client.get(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Удаление пользователя с id {}", userId);
        client.delete(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable long userId, @RequestBody UserDto userDto) {
        userDto.setId(userId);
        log.info("Обновление данных пользователя id {} data {}", userId, userDto);
        return client.update(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Запрос всех пользователей");
        return client.getAll();
    }
}
