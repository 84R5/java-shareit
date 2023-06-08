package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final long USER_ID_1 = 1L;
    private static final String USERNAME_1 = "User_1";
    private static final User USER_1 = User.builder().email("asdf@mail.org").name(USERNAME_1).id(USER_ID_1).build();
    private static final String USERNAME_2 = "User_2";
    private static final User USER_2 = User.builder().email("user2@mail.org").name(USERNAME_2).build();

    private static final long SUCCESS_ID = 2L;
    private static final long BAD_ID = 33L;
    List<User> list = new ArrayList<>();
    @Mock
    UserRepository userRepository = mock(UserRepository.class);
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
        list.add(USER_1);
        list.add(USER_2);
    }

    @Test
    void create() {
        when(userRepository.save(any(User.class))).thenAnswer((Answer<User>) invocationOnMock -> {
            User user = invocationOnMock.getArgument(0, User.class);
            user.setId(USER_ID_1);
            return user;
        });
        UserDto testUserDto = userService.create(UserMapper.toUserDto(USER_1));
        assertEquals(USER_ID_1, testUserDto.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getAll() {
        when(userRepository.findAll()).thenReturn(list);
        List<UserDto> testUsers = userService.getUsers();
        assertEquals(2, testUsers.size());
        assertTrue(testUsers.stream().anyMatch(elem -> elem.getName().equals(USERNAME_1)));
        assertTrue(testUsers.stream().anyMatch(elem -> elem.getName().equals(USERNAME_2)));
    }

    @Test
    void getByUserId_exception() {
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> userService.getUserById(BAD_ID),
                "No exception");
        assertTrue(ex.getMessage().contains("User {} not found" + BAD_ID));
    }

    @Test
    void getById() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(USER_1));
        UserDto userDto = userService.getUserById(SUCCESS_ID);
        assertEquals(userDto.getEmail(), USER_1.getEmail());
    }

    @Test
    void update_exception() {
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () ->
                        userService.update(BAD_ID, UserDto.builder().build()),
                "No exception");
        assertEquals("User {} not found" + BAD_ID, ex.getMessage());

    }
}