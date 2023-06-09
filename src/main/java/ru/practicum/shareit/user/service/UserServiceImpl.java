package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(@Valid UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("User with id = " + userId + " is not found"));
        String name = userDto.getName();
        String email = userDto.getEmail();
        user.setName(name != null && !name.isBlank() ? name : user.getName());
        if (email != null && !email.isBlank()) {
            Optional<User> oUser = userRepository.findByEmail(email);
            if (oUser.isPresent() && !oUser.get().getId().equals(userId)) {
                throw new IllegalArgumentException("User with email "+email+" already exists");
            }
            user.setEmail(email);
        }
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("User with id = " + userId + " is not found")));
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("User with id = " + userId + " is not found"));
        userRepository.deleteById(userId);
    }
}
