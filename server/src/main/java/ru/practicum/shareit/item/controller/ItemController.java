package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.Create;

import java.util.List;

/**
 * Контроллер предмет араенды.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        log.info("Создание itemDto {}", itemDto);
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("Обновление itemDto {}", itemDto);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto get(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Получение itemDto по id {}", itemId);
        return itemService.get(userId, itemId);
    }

    @GetMapping
    public List<ItemInfoDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Запрос списка предметов пользователем {} с {} по {} шт", userId, from, size);
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size);
        return itemService.getAll(userId, pageRequest);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text,
                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Поиск предметов пользователем {} по тексту {} с {} по {} шт", userId, text, from, size);
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size);
        return itemService.search(userId, text, pageRequest);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto createComment(
            @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
            @Validated({Create.class}) @RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto, itemId, userId);
    }
}
