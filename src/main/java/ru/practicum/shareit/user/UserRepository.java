package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * Репозиторий пользователей
 */
public interface UserRepository extends JpaRepository<User, Long> {
//    /**
//   Создать
//    */
//    User create(User user);
//
//    /**
//     * Получить по ID
//     */
//    User get(Long userId);
//
//    /**
//     * Удалить по ID
//     */
//    void delete(Long userId);
//
//    /**
//     * Обновить пользователя
//     */
//    User update(User user);
//
//    /**
//     * Получить всех
//     */
//    List<User> getAll();
}
