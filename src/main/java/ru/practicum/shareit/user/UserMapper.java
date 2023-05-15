package ru.practicum.shareit.user;

public class UserMapper {
    public static UserDto mapToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User mapToUser(UserDto userDto, User user) {
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        return user;
    }
}