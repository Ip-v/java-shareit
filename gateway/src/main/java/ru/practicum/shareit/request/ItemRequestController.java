package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Контроллер запросов предметов
 */
@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Validated({Create.class}) ItemRequestDto itemRequestDto) {
        log.info("Создание нового запроса {} пользователем {}", userId, itemRequestDto);
        return client.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Запрос всех запросов предметов пользователем {}", userId);
        return client.findItemRequest(userId);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Запроос списка запросов пользователем {} с {} по {} шт", userId, from, size);
        return client.getAll(userId, from, size);
    }

    @GetMapping(path = "/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable Long requestId) {
        log.info("Запроос пользователем {} данных запроса {}", userId, requestId);
        return client.getItemRequestById(userId, requestId);
    }
}
