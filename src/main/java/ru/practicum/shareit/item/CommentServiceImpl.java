package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.CommentAccessDeniedException;
import ru.practicum.shareit.exceptions.ItemAccessDeniedException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        boolean bookingApproved = bookingRepository
                .searchBookingByBookerIdAndItemIdAndEndIsBefore(userId, itemId, LocalDateTime.now())
                .stream().noneMatch(booking -> booking.getStatus().equals(BookingStatus.APPROVED));
        if (bookingApproved) {
            throw new CommentAccessDeniedException(String.format("Пользователь %d не брал в аренду предмет %d", userId,
                    itemId));
        }

        User user = userRepository.findById(userId) .orElseThrow(() ->
                new NotFoundException(String.format("Пользователь %d не найден", userId)));
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException( String.format("Предмет %d не найден ИД", itemId)));

        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        Comment commentSave = repository.save(comment);
        return CommentMapper.toCommentDto(commentSave);
    }
}
