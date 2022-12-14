package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utils.Create;

import java.util.List;

/**
 * Контроллер запросов предметов
 */
@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody @Validated({Create.class}) ItemRequestDto itemRequestDto) {
        log.info("Создание нового запроса {} пользователем {}", userId, itemRequestDto);
        return service.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Запрос всех запросов предметов пользователем {}", userId);
        return service.findItemRequest(userId);
    }

    @GetMapping(path = "/all")
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Запроос списка запросов пользователем {} с {} по {} шт", userId, from, size);
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("created").descending());
        return service.getAll(userId, pageRequest);
    }

    @GetMapping(path = "/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long requestId) {
        log.info("Запроос пользователем {} данных запроса {}", userId, requestId);
        return service.getItemRequestById(userId, requestId);
    }
}
