package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserFullDto;
import ru.practicum.shareit.user.dto.UserInputDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserFullDto> getAll() {
        List<UserFullDto> result = userRepository.findAll()
                .stream()
                .map(UserMapper::mapToFullDto)
                .collect(Collectors.toList());
        log.info("Found {} user(s).", result.size());
        return result;
    }

    @Override
    public UserFullDto getById(Long userId) {
        UserFullDto result = userRepository
                .findById(userId)
                .map(UserMapper::mapToFullDto)
                .orElseThrow(() -> new NullPointerException(String.format("User %d is not found.", userId)));
        log.info("User {} is found.", result.getId());
        return result;
    }

    @Override
    public UserFullDto create(UserInputDto userInputDto) {
        User user = UserMapper.mapToUser(userInputDto, new User());
        UserFullDto result = Optional.of(userRepository.save(user))
                .map(UserMapper::mapToFullDto)
                .orElseThrow();
        log.info("User {} {} created.", result.getId(), result.getName());
        return result;
    }

    @Override
    public UserFullDto update(UserInputDto userInputDto, Long userId) {
        User oldUser = getUserById(userId);
        UserFullDto result = Optional.of(userRepository.save(UserMapper.mapToUser(userInputDto, oldUser)))
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