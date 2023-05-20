package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserFullDto;
import ru.practicum.shareit.user.dto.UserInputDto;

import java.util.List;

public interface UserService {

    List<UserFullDto> getAll();

    UserFullDto getById(Long userId);

    UserFullDto create(UserInputDto userInputDto);

    UserFullDto update(UserInputDto userInputDto, Long id);

    void deleteById(Long userId);

    User getUserById(Long userId);
}