package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.AlreadyExistsException;

import java.util.*;

/**
 * Репозиторий пользователей
 */
@Component
@RequiredArgsConstructor
public class UserRepository {
    private final HashMap<Long, User> storage = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private long id = 1;

    /*
    Создать
     */
    public User create(User user) {
        if (emails.contains(user.getEmail())) {
            throw new AlreadyExistsException("Не уникальный email " + user);
        }
        user.setId(id++);
        storage.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    /**
     * Получить по ID
     */
    public User get(Long userId) {
        return storage.get(userId);
    }

    /**
     * Удалить по ID
     */
    public void delete(Long userId) {
        String email = storage.get(userId).getEmail();
        emails.remove(email);
        storage.remove(userId);
    }

    /**
     * Обновить пользователя
     */
    public User update(User user) {
        User old = storage.get(user.getId());
        if (user.getEmail() != null) {
            if (emails.contains(user.getEmail())) {
                throw new AlreadyExistsException("Email already exists " + user.getEmail());
            }
            emails.remove(old.getEmail());
            emails.add(user.getEmail());
        } else {
            user.setEmail(old.getEmail());
        }
        if (user.getName() == null) {
            user.setName(old.getName());
        }
        storage.put(user.getId(), user);
        return user;
    }

    /**
     * Получить всех
     */
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }
}
