package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserFullDto;
import ru.practicum.shareit.user.dto.UserInputDto;

import java.util.List;

public interface UserService {

    List<UserFullDto> getAll();

    UserFullDto getById(Long userId);

    UserFullDto create(UserInputDto user);

    UserFullDto update(UserInputDto user, Long id);

    void deleteById(Long userId);

    User getUserById(Long userId);
}