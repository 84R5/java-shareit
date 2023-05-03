package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("UserInMemoryRepository")
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, String> emails = new HashMap<>();

    private Long currentId = 0L;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(Long userId) {
        if (!users.containsKey(userId)) {
            return Optional.empty();
        }
        return Optional.of(users.get(userId));
    }

    @Override
    public Optional<User> create(User user) {
        if (emails.containsValue(user.getEmail())) {
            return Optional.empty();
        }
        user.setId(++currentId);
        users.put(user.getId(), user);
        emails.put(user.getId(), user.getEmail().toLowerCase());
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        if (!users.containsKey(user.getId())) {
            return Optional.empty();
        }
        emails.remove(user.getId());
        emails.put(user.getId(), user.getEmail().toLowerCase());
        users.put(user.getId(), user);
        return findById(user.getId());
    }

    @Override
    public void removeById(Long userId) {
        if (users.containsKey(userId)) {
            emails.remove(userId);
        }
        users.remove(userId);
    }

    @Override
    public Boolean emailIsExist(String email) {
        return emails.containsValue(email);
    }
}