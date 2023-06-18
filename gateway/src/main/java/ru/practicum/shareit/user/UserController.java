package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserInputDto;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserInputDto userDto) {
        return client.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable("userId") Long userId,
                                         @RequestBody UserInputDto userDto) {
        return client.update(userId, userDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return client.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") Long userId) {
        return client.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        client.delete(userId);
    }
}
