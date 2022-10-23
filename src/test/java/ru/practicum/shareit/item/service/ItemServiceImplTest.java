package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    ItemRepository repository;
    @Mock
    UserService userService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    ItemRequestRepository itemRequestRepository;
    @InjectMocks
    ItemServiceImpl service;

    User user;
    User user2;
    UserDto userDto;
    User commentator;
    ItemRequest request;
    Item itemWithRequest;
    Item item;
    Item updated;
    Comment comment;
    CommentDto commentDto;
    Booking lastBooking;

    @BeforeEach
    void beforeAll() {
        user = new User(1L, "user", "user@mail.ru");
        user2 = new User(2L, "user2", "user2@mail.ru");
        userDto = UserMapper.toUserDto(user);
        commentator = new User(2L, "commentator", "commentator@mail.ru");
        request = new ItemRequest(333L, "request description", user2, LocalDateTime.now());
        itemWithRequest = new Item(1L, "name", "description", true, user, request);
        item = new Item(1L, "name", "description", true, user, null);
        updated = new Item(1L, "updated name", "updated description", true, user, null);
        comment = new Comment(1L, "comment", item, commentator,
                LocalDateTime.of(2022, Month.OCTOBER, 22, 11, 11, 11));
        commentDto = CommentMapper.toCommentDto(comment);
        lastBooking = new Booking(1L, LocalDateTime.now(), LocalDateTime.MAX,
                item, user2, BookingStatus.APPROVED);
    }

    @Test
    void create() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .owner(2L)
                .build();
        when(repository.save(any())).thenReturn(item);
        when(userService.get(anyLong())).thenReturn(userDto);

        assertEquals(itemDto, service.create(1L, itemDto));
    }

    @Test
    void createWithRequest() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .owner(2L)
                .requestId(333L)
                .build();
        when(repository.save(any())).thenReturn(itemWithRequest);
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(request));

        assertEquals(request.getId(), service.create(1L, itemDto).getRequestId());
    }

    @Test
    void requestNotFound() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .owner(2L)
                .requestId(333L)
                .build();
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.create(1L, itemDto));
    }

    @Test
    void update() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));
        when(repository.save(any())).thenReturn(updated);
        when(userService.get(anyLong())).thenReturn(userDto);
        assertEquals(ItemMapper.toItemDto(updated), service.update(1L, 1L, ItemMapper.toItemDto(item)));
    }

    @Test
    void get() {
        when(repository.findById(any())).thenReturn(Optional.of(item));
        when(bookingRepository.findBookingsByItemIdAndEndIsBeforeOrderByEndDesc(any(), any()))
                .thenReturn(List.of(lastBooking));
        when(bookingRepository.findBookingsByItemIdAndStartIsAfterOrderByStartDesc(any(), any()))
                .thenReturn(List.of(lastBooking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        assertNotNull(service.get(1L, 1L));
    }

    @Test
    void getWithEmptyBookingsDates() {
        when(repository.findById(any())).thenReturn(Optional.of(item));
        when(bookingRepository.findBookingsByItemIdAndEndIsBeforeOrderByEndDesc(any(), any()))
                .thenReturn(new ArrayList<>());
        when(bookingRepository.findBookingsByItemIdAndStartIsAfterOrderByStartDesc(any(), any()))
                .thenReturn(new ArrayList<>());
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        assertNotNull(service.get(1L, 1L));
    }

    @Test
    void search() {
        when(repository.search("search text", Pageable.ofSize(10))).thenReturn(List.of(item));
        assertEquals(List.of(ItemMapper.toItemDto(item)), service.search(1L,"search text",
                Pageable.ofSize(10)));
    }

    @Test
    void isItemAvailable() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));

        assertTrue(service.isItemAvailable(1L));
    }

    @Test
    void isItemAvailable_whenItemNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.isItemAvailable(1L));
    }

    @Test
    void getAll() {
        when(repository.findAll(Pageable.ofSize(10))).thenReturn(new PageImpl<>(List.of(item)));
        when(bookingRepository.findBookingsByItemIdAndEndIsBeforeOrderByEndDesc(any(), any()))
                .thenReturn(List.of(lastBooking));
        when(bookingRepository.findBookingsByItemIdAndStartIsAfterOrderByStartDesc(any(), any()))
                .thenReturn(List.of(lastBooking));

        List<ItemInfoDto> all = service.getAll(1L, Pageable.ofSize(10));

        assertNotNull(all);
        assertEquals(1, all.size());
    }
}