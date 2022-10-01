package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис предметов
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        if (userService.get(userId) == null) {
            throw new ItemNotFoundException("Пользователь не найден с ID " + userId);
        }
        Item item = repository.create(userId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(long userId, Long itemId, ItemDto itemDto) {
        if (userService.get(userId) == null) {
            throw new ItemNotFoundException("Пользователь не найден с ID " + userId);
        }
        Item item = repository.update(userId, itemId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto get(Long itemId) {
        return ItemMapper.toItemDto(repository.get(itemId));
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        if (text == null || text.length() == 0) {
            return new ArrayList<>();
        }
        List<Item> searchResult = repository.search(text);
        return searchResult.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return repository.getAll(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
