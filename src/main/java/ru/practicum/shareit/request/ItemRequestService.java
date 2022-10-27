package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * Служба запросов
 */
public interface ItemRequestService {
    /**
     * Создание нового запроса
     */
    ItemRequestDto create(long userId, ItemRequestDto itemRequestDto);

    /**
     * <h3>Запрос запросов :)</h3>
     * Получить список своих запросов вместе с данными об ответах на них. Для каждого запроса должны указываться
     * описание, дата и время создания и список ответов в формате: id вещи, название, id владельца. Так в дальнейшем,
     * используя указанные id вещей, можно будет получить подробную информацию о каждой вещи. Запросы должны
     * возвращаться в отсортированном порядке от более новых к более старым.
     */
    List<ItemRequestDto> findItemRequest(long userId);

    /**
     * Получить список запросов, созданных другими пользователями. С помощью этого эндпоинта пользователи смогут
     * просматривать существующие запросы, на которые они могли бы ответить. Запросы сортируются по дате создания:
     * от более новых к более старым. Результаты должны возвращаться постранично. Для этого нужно передать
     * два параметра: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
     */
    List<ItemRequestDto> getAll(long userId, Pageable pageRequest);

    /**
     *  Получить данные об одном конкретном запросе вместе с данными об ответах на него в том же формате,
     *  что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.
     */
    ItemRequestDto getItemRequestById(Long userId, Long requestId);
}
