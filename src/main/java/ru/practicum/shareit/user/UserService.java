package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    List<UserDto> getAll();

    UserDto getById(Long userId);

    UserDto create(UserDto user);

    UserDto update(UserDto user, Long id);

    void deleteById(Long userId);

    User getUserById(Long userId);
}