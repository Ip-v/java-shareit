package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.BookingChangeStatusAfterApproveException;
import ru.practicum.shareit.exceptions.ItemAccessDeniedException;
import ru.practicum.shareit.exceptions.ItemNotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Cервис бронирований
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto create(long userId, BookingDto bookingDto) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ИД " + userId));
        final Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден с ИД " + bookingDto.getItemId()));
        if (!item.getIsAvailable()) {
            throw new ItemNotAvailableException("Предмет не доступен для бронирования ИД " + bookingDto.getItemId());
        }
        if (item.getOwner().getId() == userId) {
            throw new ItemAccessDeniedException("Доступ к бронированию запрещен");
        }
        final Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        final Booking save = repository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto update(long userId, Long itemId, BookingDto bookingDto) {
        final User booker = userRepository.findById(bookingDto.getBookerId()).orElseThrow(() ->
                new NotFoundException("Пользователь не найден с ИД " + userId));
        Booking booking = BookingMapper.toBooking(get(bookingDto.getId()));
        if (bookingDto.getStart() != null) {
            booking.setStart(bookingDto.getStart());
        }
        if (bookingDto.getEnd() != null) {
            booking.setEnd(bookingDto.getEnd());
        }
        if (bookingDto.getBookerId() != null) {
            booking.setBooker(booker);
        }
        if (bookingDto.getStatus() != null) {
            booking.setStatus(bookingDto.getStatus());
        }
        Booking save = repository.save(booking);
        return BookingMapper.toBookingDto(save);
    }

    @Override
    public BookingDto get(Long bookingId) {
        Optional<Booking> booking = repository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NotFoundException("Бронирование не найдено ID " + bookingId);
        }
        return BookingMapper.toBookingDto(booking.get());
    }

    @Override
    public List<BookingInfoDto> getAll(Long userId, BookingStatus status) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ИД " + userId));

        switch (status) {
            case CURRENT:
                return repository
                        .findCurrentBookingsByBooker(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case PAST:
                return repository
                        .findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return repository
                        .findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case WAITING:
                return repository
                        .findBookingsByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return repository
                        .findBookingsByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            default:
                return repository
                        .findByBookerIdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
        }
    }

    @Override
    public List<BookingDto> search(Long userId, String text) {
        return null;
    }

    @Override
    public BookingInfoDto getBookingById(Long userId, Long bookingId) {
        Booking booking = repository.findById(bookingId).orElseThrow(() -> new NotFoundException(
                String.format("Бронирование не найдено ИД %s", bookingId)));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ItemAccessDeniedException(String.format("Нет доступа к бронированию %s у пользователя %s",
                    bookingId, userId));
        }
        return BookingMapper.toBookingInfoDto(booking);
    }

    @Override
    public List<BookingInfoDto> getOwnerBookings(long userId, BookingStatus status) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь %s не найден.", userId)));
        List<BookingInfoDto> userBookings = repository.searchBookingByItemOwnerId(userId)
                .stream()
                .map(BookingMapper::toBookingInfoDto)
                .collect(Collectors.toList());

        if (userBookings.isEmpty()) {
            throw new NotFoundException("У пользователя нет бронирований");
        }

        switch (status) {
            case CURRENT:
                return repository
                        .findCurrentBookingsByOwner(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case PAST:
                return repository
                        .findBookingsByItemOwnerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return repository
                        .searchBookingByItemOwnerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case WAITING:
                return repository
                        .findBookingsByItemOwnerIdOrderByStartDesc(userId)
                        .stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.WAITING))
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return repository
                        .findBookingsByItemOwnerIdOrderByStartDesc(userId).stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.REJECTED))
                        .map(BookingMapper::toBookingInfoDto)
                        .collect(Collectors.toList());
            default:
                userBookings.sort(Comparator.comparing(BookingInfoDto::getId).reversed());
                return userBookings;
        }
    }

    @Override
    @Transactional
    public BookingInfoDto approveBooking(long userId, Long bookingId, boolean approved) {
        final Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено ИД " + bookingId));
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BookingChangeStatusAfterApproveException("Бронирование уже подвтрждено");
        }
        final Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден с ИД " + booking.getItem().getId()));
        if (userId != item.getOwner().getId()) {
            throw new ItemAccessDeniedException("Подтвердить бронирование может только владелец предмета.");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking save = repository.save(booking);
        return new BookingInfoDto(save.getId(), save.getStart(), save.getEnd(), save.getBooker(), save.getItem(),
                save.getStatus());
    }
}