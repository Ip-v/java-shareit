package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;

    Item item;
    ItemRequest request;
    User user;
    User user2;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(new User(1L, "user", "user@mail.ru"));
        user2 = userRepository.save(new User(2L, "user2", "user2@mail.ru"));
        request = itemRequestRepository.save(new ItemRequest(333L, "request description", user2,
                LocalDateTime.now()));
        item = itemRepository.save(new Item(1L, "name", "description", true, user, request));
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void search() {
        final List<Item> list = itemRepository.search("descr", Pageable.unpaged());

        assertNotNull(list);
        assertEquals(item.getDescription(), list.get(0).getDescription());
    }

    @Test
    void findItemByRequestId() {
        final List<Item> list = itemRepository.findItemByRequestId(2L);

        assertNotNull(list);
        assertEquals(item.getDescription(), list.get(0).getDescription());
    }
}