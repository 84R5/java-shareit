package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.user.EmailConflictException;
import ru.practicum.shareit.exception.user.UserCreationException;
import ru.practicum.shareit.exception.user.UserNotFoundException;

import java.util.List;
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
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
        log.info("Found {} user(s).", result.size());
        return result;
    }

    public UserDto getById(Long userId) {
        UserDto result = userRepository
                .findById(userId)
                .map(UserMapper::mapToDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
        log.info("User {} is found.", result.getId());
        return result;
    }

    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto, new User());
        UserDto result = userRepository
                .create(user)
                .map(UserMapper::mapToDto)
                .orElseThrow(() -> new UserCreationException(user.getName()));
        log.info("User {} {} created.", result.getId(), result.getName());
        return result;
    }

    public UserDto update(UserDto newUser, Long userId) {
        User oldUser = getUserById(userId);
        String email = newUser.getEmail();
        if (email != null
                && !oldUser.getEmail().equalsIgnoreCase(email)
                && userRepository.emailIsExist(email)) {
            log.warn("User's email {} is wrong.", newUser.getEmail());
            throw new EmailConflictException(newUser.getEmail());
        }
        UserDto result = userRepository
                .update(UserMapper.mapToUser(newUser, oldUser))
                .map(UserMapper::mapToDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
        log.info("User {} {} updated.", result.getId(), result.getName());
        return result;
    }

    public void deleteById(Long userId) {
        User result = getUserById(userId);
        userRepository.removeById(result.getId());
        log.info("User {} removed.", result.getName());
    }

    public User getUserById(Long userId) {
        User result = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        log.info("User {} is found.", result.getId());
        return result;
    }
}