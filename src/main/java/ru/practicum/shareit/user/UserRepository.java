package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Репозиторий пользователей
 */
public interface UserRepository {
    /**
   Создать
    */
    User create(User user);

    /**
     * Получить по ID
     */
    User get(Long userId);

    /**
     * Удалить по ID
     */
    void delete(Long userId);

    /**
     * Обновить пользователя
     */
    User update(User user);

    /**
     * Получить всех
     */
    List<User> getAll();
}
