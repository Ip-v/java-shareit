package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

/**
 * Репозиторий пользователей
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final HashMap<Long, User> storage = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private long id = 1;

    @Override
    public User create(User user) {
        if (emails.contains(user.getEmail())) {
            throw new AlreadyExistsException("Не уникальный email " + user);
        }
        user.setId(id++);
        storage.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User get(Long userId) {
        return storage.get(userId);
    }

    @Override
    public void delete(Long userId) {
        String email = storage.get(userId).getEmail();
        emails.remove(email);
        storage.remove(userId);
    }

    @Override
    public User update(User userRecordUpdate) {
        User user = storage.get(userRecordUpdate.getId());
        if (userRecordUpdate.getEmail() != null
                && !user.getEmail().equalsIgnoreCase(userRecordUpdate.getEmail())) {
            if (emails.contains(userRecordUpdate.getEmail())) {
                throw new AlreadyExistsException("Email already exists " + userRecordUpdate.getEmail());
            }
            emails.remove(user.getEmail());
            emails.add(userRecordUpdate.getEmail());
        } else {
            userRecordUpdate.setEmail(user.getEmail());
        }
        if (userRecordUpdate.getName() == null) {
            userRecordUpdate.setName(user.getName());
        }
        storage.put(userRecordUpdate.getId(), userRecordUpdate);
        return userRecordUpdate;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }
}
