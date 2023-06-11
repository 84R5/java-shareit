package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UserServiceImplTest {

    @Mock
    private UserRepository mockUserRepository;
    @InjectMocks
    private UserServiceImpl service;

    @Test
    void createUser_shouldCreateAUser() {
        UserDto userDto = initUser();
        User user = UserMapper.toUser(userDto);
        when(mockUserRepository.save(user)).thenReturn(user);

        assertEquals(userDto, service.create(userDto));
        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    void updateUser_shouldUpdateUserName() {
        UserDto userDto = initUser();
        UserDto uUser = UserDto.builder()
                .id(1L)
                .name("Max")
                .build();
        User user = UserMapper.toUser(userDto);
        when(mockUserRepository.save(user)).thenReturn(user);
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));

        userDto.setName("Max");

        assertEquals(userDto, service.update(uUser.getId(),uUser));
    }

    @Test
    void updateUser_shouldUpdateUserEmail() {
        UserDto userDto = initUser();
        User user = UserMapper.toUser(userDto);
        UserDto uUser = UserDto.builder()
                .id(1L)
                .email("new@email.ru")
                .build();
        when(mockUserRepository.save(user)).thenReturn(user);
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));

        userDto.setEmail("new@email.ru");

        assertEquals(userDto, service.update(uUser.getId(),uUser));
    }

    @Test
    void getUserById_shouldReturnUserById() {
        UserDto userDto = initUser();
        User user = UserMapper.toUser(userDto);
        when(mockUserRepository.save(user)).thenReturn(user);
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(userDto, service.update(userDto.getId(),userDto));
        verify(mockUserRepository, times(1)).findById(any());
    }

    @Test
    void getUserById_shouldThrowIfUserDoesNotExists() {
        when(mockUserRepository.findById(any())).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () ->
                service.getUserById(1L));
        assertEquals("User with id = 1 is not found", exception.getMessage());
    }

    @Test
    void getUsers_shouldReturnListOfUsers() {
        UserDto user1 = initUser();
        UserDto user2 = UserDto.builder()
                .id(2L)
                .name("Leon")
                .email("new@mail.ru")
                .build();
        List<User> users = Stream.of(user1,user2).map(UserMapper::toUser).collect(Collectors.toList());
        when(mockUserRepository.findAll()).thenReturn(users);

        assertEquals(List.of(user1, user2), service.getUsers());
        verify(mockUserRepository, times(1)).findAll();
    }

    @Test
    void removeUserById_shouldRemoveUser() {
        UserDto userDto = initUser();
        User user = UserMapper.toUser(userDto);
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));
        service.delete(1L);
        verify(mockUserRepository, times(1)).deleteById(any());

    }

    private UserDto initUser() {
        return UserDto.builder()
                .id(1L)
                .name("Nikolas")
                .email("nik@gmail.com")
                .build();
    }
}