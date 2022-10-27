package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository repository;
    @InjectMocks
    private ItemRequestServiceImpl service;
    private ItemRequestDto itemRequestDto;
    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "owner", "owner@mail.ru");
        itemRequestDto = new ItemRequestDto(1L, "request description",
                1L, LocalDateTime.now(), null);
        itemRequest = new ItemRequest(1L, "request description", user, null);
    }

    @Test
    void exceptionWhenUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.create(2L, itemRequestDto));
    }

    @Test
    void createTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(itemRequest);

        assertEquals(itemRequestDto.getId(), service.create(1L, itemRequestDto).getId());
        assertEquals(itemRequestDto.getDescription(), service.create(1L, itemRequestDto).getDescription());
    }

    @Test
    void findItemRequest() {
        exceptionWhenUserNotFound();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.findUserRequests(1L)).thenReturn(List.of(itemRequest));
        when(itemRepository.findItemByRequestId(1L)).thenReturn(new ArrayList<>());

        List<ItemRequestDto> requests = service.findItemRequest(1L);
        assertNotNull(requests);
        assertEquals(1, requests.size());

        verify(repository, times(1)).findUserRequests(1L);
    }

    @Test
    void getAll() {
        when(repository.findRequests(anyLong(), any())).thenReturn(List.of(itemRequest));
        when(itemRepository.findItemByRequestId(1L)).thenReturn(new ArrayList<>());

        List<ItemRequestDto> requests = service.getAll(1L, Pageable.unpaged());
        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertNotNull(itemRequestDto.getDescription(), requests.get(0).getDescription());
    }

    @Test
    void getItemRequestByIdWhenUserOrRequestNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getItemRequestById(2L, 2L));
        assertThrows(NotFoundException.class, () -> service.getItemRequestById(1L, 2L));
    }

    @Test
    void getItemRequestByIdTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.findById(1L)).thenReturn(Optional.of(itemRequest));

        assertEquals(itemRequest.getId(), service.getItemRequestById(1L, 1L).getId());
    }
}