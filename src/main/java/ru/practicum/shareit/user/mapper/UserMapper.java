package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserFullDto;
import ru.practicum.shareit.user.dto.UserInputDto;
import ru.practicum.shareit.user.dto.UserShortDto;

public class UserMapper {
    public static UserFullDto mapToFullDto(User user) {
        return new UserFullDto(user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static UserShortDto mapToShortDto(User user) {
        return new UserShortDto(user.getId(),
                user.getName());
    }


    public static User mapToUser(UserInputDto userInputDto, User user) {
        if (userInputDto.getId() != null) {
            user.setId(userInputDto.getId());
        }

        if (userInputDto.getName() != null) {
            user.setName(userInputDto.getName());
        }

        if (userInputDto.getEmail() != null) {
            user.setEmail(userInputDto.getEmail());
        }

        return user;
    }
}