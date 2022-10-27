package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository repository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private ItemRequest itemRequest;
    private ItemRequest itemRequest2;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(new User(1L, "user", "user@mail.ru"));
        User user2 = userRepository.save(new User(2L, "user2", "user2@mail.ru"));
        itemRequest = repository.save(new ItemRequest(1L, "request description", user, null));
        itemRequest2 = repository.save(new ItemRequest(2L, "request description", user2, null));
    }

    @Test
    void findUserRequests() {
        final List<ItemRequest> list = repository.findUserRequests(user.getId());

        assertEquals(itemRequest.getId(), list.get(0).getId());
    }

    @Test
    void findRequests() {
        final List<ItemRequest> list = repository.findRequests(user.getId(), Pageable.unpaged());

        assertEquals(itemRequest2.getRequester().getId(), list.get(0).getRequester().getId());
    }
}