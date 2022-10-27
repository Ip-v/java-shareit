package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.ItemAccessDeniedException;
import ru.practicum.shareit.exceptions.NotFoundException;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemInfoDto.BookingInfo;

/**
 * Сервис предметов
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemDto create(long userId, ItemDto itemDto) {
        final User user = UserMapper.toUser(userService.get(userId));
        final Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository
                    .findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос предмета с ИД не найден " +
                            itemDto.getRequestId()));
            item.setRequest(itemRequest);
        }
        final Item save = repository.save(item);
        return ItemMapper.toItemDto(save);
    }

    @Override
    @Transactional
    public ItemDto update(long userId, Long itemId, ItemDto itemDto) {
        final UserDto userDto = userService.get(userId);
        final User user = UserMapper.toUser(userDto);
        Optional<Item> itemOpt = repository.findById(itemId);
        if (itemOpt.isEmpty()) {
            throw new NotFoundException("Предмет не найден ID " + userId);
        }
        final Item item = itemOpt.get();
        if (item.getOwner() == null || !user.equals(item.getOwner())) {
            throw new ItemAccessDeniedException("Доступ запрещен.");
        }
        if (itemDto.getAvailable() != null) {
            item.setIsAvailable(itemDto.getAvailable());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        Item save = repository.save(item);
        return ItemMapper.toItemDto(save);
    }

    @Override
    public ItemInfoDto get(Long userId, Long itemId) {
        Item item = repository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Предмет не найден ИД " + itemId));
        ItemInfoDto itemInfoDto = ItemMapper.toItemInfoDto(item);
        if (item.getOwner().getId().equals(userId)) {
            addBookingsDates(itemInfoDto);
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        if (!comments.isEmpty()) {
            itemInfoDto.setComments(comments.stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList())
            );
        }
        return itemInfoDto;

    }

    @Override
    public List<ItemDto> search(Long userId, String text, Pageable pageRequest) {
        if (text == null || text.length() == 0) {
            return new ArrayList<>();
        }
        List<Item> searchResult = repository.search(text, pageRequest);
        return searchResult.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public boolean isItemAvailable(Long itemId) {
        Optional<Item> itemOpt = repository.findById(itemId);
        if (itemOpt.isPresent()) {
            return itemOpt.get().getIsAvailable();
        }
        throw new NotFoundException("Предмет не найден ИД " + itemId);
    }

    @Override
    public List<ItemInfoDto> getAll(Long userId, Pageable pageRequest) {
        List<ItemInfoDto> items = repository.findAll(pageRequest).stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(ItemMapper::toItemInfoDto)
                .collect(Collectors.toList());
        for (ItemInfoDto i : items) {
            addBookingsDates(i);
        }
        items.sort(Comparator.comparing(ItemInfoDto::getId));
        return items;
    }

    private void addBookingsDates(ItemInfoDto item) {
        List<Booking> lastBookings = bookingRepository
                .findBookingsByItemIdAndEndIsBeforeOrderByEndDesc(item.getId(),
                        LocalDateTime.now());
        if (!lastBookings.isEmpty()) {
            item.setLastBooking(new BookingInfo(lastBookings.get(0)));
        }
        List<Booking> nextBookings = bookingRepository
                .findBookingsByItemIdAndStartIsAfterOrderByStartDesc(item.getId(), LocalDateTime.now());
        if (!nextBookings.isEmpty()) {
            item.setNextBooking(new BookingInfo(nextBookings.get(0)));
        }
    }
}
