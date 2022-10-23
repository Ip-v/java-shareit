package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository repository;

    @InjectMocks
    CommentServiceImpl service;

    User user;
    Item item;
    Booking booking;
    CommentDto commentDto;
    Comment comment;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "user", "user@mail.ru");
        item = new Item(1L, "name", "description", true, user, null);
        booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.MAX, item, user,
                BookingStatus.APPROVED);
        commentDto = new CommentDto(1L, "comment text", "Name", null);
        comment = new Comment(1L, "comment text", item, user,
                LocalDateTime.of(2022, Month.OCTOBER, 22, 11, 11, 11));
    }


    @Test
    void createComment() {
        when(bookingRepository.searchBookingByBookerIdAndItemIdAndEndIsBefore(anyLong(), anyLong(), any()))
                .thenReturn(List.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(repository.save(any())).thenReturn(comment);

        CommentDto serviceComment = service.createComment(commentDto, 1L, 1L);
        assertNotNull(serviceComment);
        assertEquals(commentDto.getText(), serviceComment.getText());
    }

    @Test
    void userNotFound() {
        when(bookingRepository.searchBookingByBookerIdAndItemIdAndEndIsBefore(anyLong(), anyLong(), any()))
                .thenReturn(List.of(booking));
        when(userRepository.findById(-1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.createComment(commentDto, 1L, -1L));
        assertEquals("Пользователь -1 не найден", exception.getMessage());
    }

    @Test
    void userItemNotFound() {
        when(bookingRepository.searchBookingByBookerIdAndItemIdAndEndIsBefore(anyLong(), anyLong(), any()))
                .thenReturn(List.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(-1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.createComment(commentDto, -1L, 1L));
        assertEquals("Предмет -1 не найден ИД", exception.getMessage());
    }
}