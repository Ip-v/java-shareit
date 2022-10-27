package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private User user;

    @BeforeEach
    void beforeEach() {
        repository = mock(UserRepository.class);
        service = new UserService(repository);
        user = new User(1L, "user", "user@mail.ru");
    }

    @Test
    void create() {
        when(repository.save(any())).thenReturn(user);
        UserDto dto = service.create(UserMapper.toUserDto(user));
        assertNotNull(dto);
        assertEquals(UserMapper.toUserDto(user), dto);
    }

    @Test
    void get() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        assertEquals(UserMapper.toUserDto(user), service.get(user.getId()));
    }

    @Test
    void delete() {
        service.delete(user.getId());
        verify(repository, Mockito.times(1)).deleteById(user.getId());
    }

    @Test
    void updateUserName() {
        UserDto inputUserDto = new UserDto(1L, "UpdateUser", null);
        User updatedUser = new User(1L, "updatedUser", "user@mail.ru");

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(updatedUser);

        UserDto userDto = service.update(inputUserDto);

        assertEquals("updatedUser", userDto.getName());
    }

    @Test
    void updateUserEmail() {
        UserDto inputUserDto = new UserDto(1L, null, "updated@mail.ru");
        User updatedUser = new User(1L, "user", "updated@mail.ru");

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(updatedUser);

        UserDto userDto = service.update(inputUserDto);

        assertEquals("updated@mail.ru", userDto.getEmail());
    }

    @Test
    void getUserNotFound() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.get(-1L));
        assertEquals("Пользователь не найден -1", exception.getMessage());
    }

    @Test
    void updateUserNotFound() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.update(UserMapper.toUserDto(user)));
        assertEquals("Пользователь не найден 1", exception.getMessage());
    }

    @Test
    void getAll() {
        when(repository.findAll()).thenReturn(List.of(user));

        assertEquals(1, service.getAll().size());
        assertEquals(List.of(UserMapper.toUserDto(user)), service.getAll());
    }
}