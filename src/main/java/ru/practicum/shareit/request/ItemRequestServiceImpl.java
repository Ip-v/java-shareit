package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto create(long userId, ItemRequestDto itemRequestDto) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ИД " + userId));
        final ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        final ItemRequest save = repository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(save);
    }

    @Override
    public List<ItemRequestDto> findItemRequest(long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ИД " + userId));
        List<ItemRequestDto> requests = repository.findUserRequests(userId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        if (requests.size() > 0) {
            for (ItemRequestDto request : requests) {
                request.setItems(ItemMapper.toItemDtoList(itemRepository.findItemByRequestId(request.getId())));
            }
        }
        return requests;
    }

    @Override
    public List<ItemRequestDto> getAll(long userId, PageRequest pageRequest) {
        List<ItemRequestDto> requests = repository
                .findRequests(userId, pageRequest)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        if (requests.size() > 0) {
            for (ItemRequestDto request : requests) {
                request.setItems(ItemMapper.toItemDtoList(itemRepository.findItemByRequestId(request.getId())));
            }
        }
        return requests;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден с ИД " + userId));
        ItemRequest request = repository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с ИД {} не найден"));
        ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(request);
        dto.setItems(ItemMapper.toItemDtoList(itemRepository.findItemByRequestId(request.getId())));
        return dto;
    }
}
