package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

public interface UserService {

    UserDto create(@Valid UserDto userDto);

    UserDto update(Long userId, UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getUsers();

    void delete(Long userId);
}
