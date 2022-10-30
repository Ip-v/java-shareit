package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    private User owner;
    private Booking booking;

    @BeforeEach
    void beforeEach() {
        owner = userRepository.save(new User(1L, "user", "user@mail.ru"));
        User booker = userRepository.save(new User(2L, "user2", "user2@mail.ru"));
        Item item = itemRepository.save(new Item(1L, "name", "description", true, owner,
                null));
        booking = bookingRepository.save(new Booking(1L, LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now().plusMinutes(10), item, booker, BookingStatus.WAITING));
    }

    @Test
    void findCurrentBookingsByOwnerTest() {
        final List<Booking> list = bookingRepository.findCurrentBookingsByOwner(owner.getId(), LocalDateTime.now(),
                Pageable.unpaged());

        assertEquals(booking.getBooker().getId(), list.get(0).getBooker().getId());
    }

    @Test
    void findCurrentBookingsByBooker() {
        final List<Booking> list = bookingRepository.findCurrentBookingsByBooker(2L, LocalDateTime.now(),
                Pageable.ofSize(10));

        assertEquals(booking.getBooker().getId(), list.get(0).getBooker().getId());
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        bookingRepository.deleteAll();
    }
}