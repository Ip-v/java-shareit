package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.BookingChangeStatusAfterApproveException;
import ru.practicum.shareit.exceptions.ItemAccessDeniedException;
import ru.practicum.shareit.exceptions.ItemNotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    BookingRepository repository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    BookingServiceImpl service;
    User user;
    Item item;
    BookingInfoDto.ItemInfo itemInfo;
    BookingInfoDto bookingInfoDto;
    BookingDto bookingDto;
    Booking booking;
    Booking bookingCurrent;
    Booking bookingPast;
    Booking bookingFuture;
    Booking bookingWaiting;
    Booking bookingRejected;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "user", "user@mail.ru");
        item = new Item(1L, "name", "description", true, user, null);
        itemInfo = new BookingInfoDto.ItemInfo(item);
        booking = new Booking(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, item, user,
                BookingStatus.WAITING);
        bookingInfoDto = new BookingInfoDto(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, user,
                itemInfo, BookingStatus.WAITING);
        bookingDto = new BookingDto(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, 1L,
                1L, BookingStatus.WAITING);

        bookingCurrent = new Booking(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, item, user,
                BookingStatus.CURRENT);
        bookingPast = new Booking(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, item, user,
                BookingStatus.PAST);
        bookingFuture = new Booking(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, item, user,
                BookingStatus.FUTURE);
        bookingWaiting = new Booking(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, item, user,
                BookingStatus.WAITING);
        bookingRejected = new Booking(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, item, user,
                BookingStatus.REJECTED);
    }

    @Test
    void create() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(repository.save(any())).thenReturn(booking);

        BookingInfoDto dto = service.create(2L, bookingDto);
        assertNotNull(dto);
        assertEquals(booking.getId(), dto.getId());

        assertThrows(ItemAccessDeniedException.class, () -> service.create(1L, bookingDto));

        item.setIsAvailable(false);
        assertThrows(ItemNotAvailableException.class, () -> service.create(2L, bookingDto));
    }

    @Test
    void createWhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.create(1L, bookingDto));
    }

    @Test
    void update() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(repository.save(any())).thenReturn(booking);

        BookingDto dto = service.update(2L, 1L, bookingDto);
        assertEquals(booking.getId(), dto.getId());
    }

    @Test
    void get() {
        when(repository.findById(1L)).thenReturn(Optional.of(booking));
        when(repository.findById(2L)).thenReturn(Optional.empty());

        BookingDto dto = service.get(1L);
        assertEquals(booking.getId(), dto.getId());

        assertThrows(NotFoundException.class, () -> service.get(2L));

        verify(repository, times(2)).findById(anyLong());
    }

    @Test
    void getAll() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getAll(2L, BookingStatus.WAITING, Pageable.unpaged()));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.findCurrentBookingsByBooker(anyLong(), any(), any())).thenReturn(List.of(bookingCurrent));
        when(repository.findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(bookingPast));
        when(repository.findByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(bookingFuture));
        when(repository.findBookingsByBookerIdAndStatusOrderByStartDesc(1L, BookingStatus.WAITING,
                Pageable.unpaged()))
                .thenReturn(List.of(bookingWaiting));
        when(repository.findBookingsByBookerIdAndStatusOrderByStartDesc(1L, BookingStatus.REJECTED,
                Pageable.unpaged()))
                .thenReturn(List.of(bookingRejected));
        when(repository.findAllByBookerIdOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));

        assertEquals(BookingStatus.CURRENT, service.getAll(1L, BookingStatus.CURRENT,
                Pageable.unpaged()).get(0).getStatus());
        assertEquals(BookingStatus.PAST, service.getAll(1L, BookingStatus.PAST,
                Pageable.unpaged()).get(0).getStatus());
        assertEquals(BookingStatus.FUTURE, service.getAll(1L, BookingStatus.FUTURE,
                Pageable.unpaged()).get(0).getStatus());
        assertEquals(BookingStatus.WAITING, service.getAll(1L, BookingStatus.WAITING,
                Pageable.unpaged()).get(0).getStatus());
        assertEquals(BookingStatus.REJECTED, service.getAll(1L, BookingStatus.REJECTED,
                Pageable.unpaged()).get(0).getStatus());
        assertEquals(BookingStatus.WAITING, service.getAll(1L, BookingStatus.ALL,
                Pageable.unpaged()).get(0).getStatus());
    }

    @Test
    void getBookingById() {
        when(repository.findById(1L)).thenReturn(Optional.of(booking));
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getBookingById(2L, 2L));

        assertEquals(booking.getItem().getName(), service.getBookingById(1L, 1L).getItem().getName());
        assertThrows(ItemAccessDeniedException.class, () -> service.getBookingById(2L, 1L));
    }

    @Test
    void getOwnerBookingsWhenUserHasNoBookings() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.searchBookingByItemOwnerId(1L, Pageable.unpaged())).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class,
                () -> service.getOwnerBookings(1L, BookingStatus.WAITING, Pageable.unpaged()));
    }

    @Test
    void getOwnerBookings() {
        User user2 = new User(2L, "user2", "user2@mail.ru");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(repository.searchBookingByItemOwnerId(1L, Pageable.unpaged())).thenReturn(List.of(booking));
        when(repository.searchBookingByItemOwnerId(2L, Pageable.unpaged())).thenReturn(List.of(booking));

        when(repository.findCurrentBookingsByOwner(anyLong(), any(), any())).thenReturn(List.of(bookingCurrent));
        when(repository.findBookingsByItemOwnerIdAndEndIsBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(bookingPast));
        when(repository.searchBookingByItemOwnerIdAndStartIsAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(bookingFuture));
        when(repository.findBookingsByItemOwnerIdOrderByStartDesc(1L, Pageable.unpaged()))
                .thenReturn(List.of(bookingWaiting));
        when(repository.findBookingsByItemOwnerIdOrderByStartDesc(2L, Pageable.unpaged()))
                .thenReturn(List.of(bookingRejected));

        assertEquals(BookingStatus.CURRENT, service.getOwnerBookings(1L, BookingStatus.CURRENT,
                Pageable.unpaged()).get(0).getStatus());
        assertEquals(BookingStatus.PAST, service.getOwnerBookings(1L, BookingStatus.PAST,
                Pageable.unpaged()).get(0).getStatus());
        assertEquals(BookingStatus.FUTURE, service.getOwnerBookings(1L, BookingStatus.FUTURE,
                Pageable.unpaged()).get(0).getStatus());
        assertEquals(BookingStatus.WAITING, service.getOwnerBookings(1L, BookingStatus.WAITING,
                Pageable.unpaged()).get(0).getStatus());
        assertEquals(BookingStatus.REJECTED, service.getOwnerBookings(2L, BookingStatus.REJECTED,
                Pageable.unpaged()).get(0).getStatus());
        assertEquals(BookingStatus.WAITING, service.getOwnerBookings(1L, BookingStatus.ALL,
                Pageable.unpaged()).get(0).getStatus());
    }

    @Test
    void approveBookingWhenBookingNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.approveBooking(1L, 2L, true));
    }

    @Test
    void approveBookingWhenBookingAlreadyApproved() {
        booking.setStatus(BookingStatus.APPROVED);
        when(repository.findById(1L)).thenReturn(Optional.of(booking));
        assertThrows(BookingChangeStatusAfterApproveException.class,
                () -> service.approveBooking(1L, 1L, true));
    }

    @Test
    void approveBookingWhenItemNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> service.approveBooking(1L, 1L, true));
    }

    @Test
    void approveBookingWhenAccessDenied() {
        when(repository.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(ItemAccessDeniedException.class,
                () -> service.approveBooking(2L, 1L, true));
    }

    @Test
    void approveBooking() {
        when(repository.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(repository.save(any())).thenReturn(booking);
        assertEquals(BookingStatus.APPROVED, service.approveBooking(1L, 1L, true).getStatus());
    }
}