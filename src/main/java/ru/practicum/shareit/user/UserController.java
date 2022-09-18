package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;

import java.util.List;

/**
 * Контроллер пользователей
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable Long userId) {
        return userService.get(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId, @RequestBody UserDto userDto) {
        userDto.setId(userId);
        return userService.update(userDto);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }
}
