package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.ItemAccessDeniedException;
import ru.practicum.shareit.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Репозиторий предмет араенды.
 */
@Component
@RequiredArgsConstructor
public class ItemRepository {
    private final Map<Long, Item> repository = new HashMap<>();
    private long id = 1;

    /**
     * Добавление нового предмета
     */
    public Item create(long userId, Item item) {
        item.setId(id++);
        item.setOwner(userId);
        repository.put(item.getId(), item);
        return item;
    }

    /**
     * Обновление существующего предмета
     */
    public Item update(long userId, Long itemId, Item item) {
        if (!repository.containsKey(itemId)) {
            throw new ItemNotFoundException("Предмет не найден в хранилище. ИД " + itemId);
        }
        Item oldItem = repository.get(itemId);
        if (oldItem == null || oldItem.getOwner() == null || userId != oldItem.getOwner()) {
            throw new ItemAccessDeniedException("Доступ запрещен.");
        }
        if (item.getIsAvailable() != null) {
            oldItem.setIsAvailable(item.getIsAvailable());
        }
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        repository.put(itemId, oldItem);
        return oldItem;
    }

    /**
     * Получение предмета по id
     */
    public Item get(Long itemId) {
        return repository.get(itemId);
    }

    /**
     * Получение всех предметов.
     */
    public List<Item> getAll(Long userId) {
        return repository.values().stream().filter(i -> i.getOwner() == userId).collect(Collectors.toList());
    }

    /**
     * Поиск доступных предметов
     */
    public List<Item> search(String text) {
        return repository.values().stream()
                .filter(i -> (i.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        i.getName().toLowerCase().contains(text.toLowerCase())) &&
                        i.getIsAvailable())
                .collect(Collectors.toList());
    }
}
