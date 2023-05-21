package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        List<UserDto> result = userRepository.findAll()
                .stream()
                .map(UserMapper::mapToFullDto)
                .collect(Collectors.toList());
        log.info("Found {} user(s).", result.size());
        return result;
    }

    @Override
    public UserDto getById(Long userId) {
        UserDto result = userRepository
                .findById(userId)
                .map(UserMapper::mapToFullDto)
                .orElseThrow(() -> new NullPointerException(String.format("User %d is not found.", userId)));
        log.info("User {} is found.", result.getId());
        return result;
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto, new User());
        UserDto result = Optional.of(userRepository.save(user))
                .map(UserMapper::mapToFullDto)
                .orElseThrow();
        log.info("User {} {} created.", result.getId(), result.getName());
        return result;
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        User oldUser = getUserById(userId);
        UserDto result = Optional.of(userRepository.save(UserMapper.mapToUser(userDto, oldUser)))
                .map(UserMapper::mapToFullDto)
                .orElseThrow(() -> new NullPointerException(String.format("User %d is not found.", userId)));
        log.info("User {} {} updated.", result.getId(), result.getName());
        return result;
    }

    @Override
    public void deleteById(Long userId) {
        User result = getUserById(userId);
        userRepository.deleteById(result.getId());
        log.info("User {} removed.", result.getName());
    }

    @Override
    public User getUserById(Long userId) {
        User result = userRepository
                .findById(userId)
                .orElseThrow(() -> new NullPointerException(String.format("User %d is not found.", userId)));
        log.info("User {} is found.", result.getId());
        return result;
    }
}