package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    private final UserService userService;

    @Test
    void getById_returnUserWith3Id_added3User() {
        addData();
        assertThat(userService.getById(3L))
                .hasFieldOrPropertyWithValue("name", "Alex")
                .hasFieldOrPropertyWithValue("email", "lex@ya.ru");
    }

    @Test
    void create_returnUserWith2Id_added3User() {
        addData();
        assertThat(userService.getById(2L))
                .hasFieldOrPropertyWithValue("name", "Denis")
                .hasFieldOrPropertyWithValue("email", "redis@yandex.ru");
    }

    @Test
    void getAll_returnAllUsers_added3Users() {
        addData();
        assertThat(userService.getAll().size()).isEqualTo(3);
    }

    @Test
    void update_returnUpdatedUser_added3Users() {
        addData();
        assertThat(userService.getById(2L))
                .hasFieldOrPropertyWithValue("name", "Denis")
                .hasFieldOrPropertyWithValue("email", "redis@yandex.ru");
        userService.update(new UserDto("Mc", "dudu@yandex.ru"), 2L);
        assertThat(userService.getById(2L))
                .hasFieldOrPropertyWithValue("name", "Mc")
                .hasFieldOrPropertyWithValue("email", "dudu@yandex.ru");
    }

    @Test
    void delete_return2Users_added3Users() {
        addData();
        assertThat(userService.getAll().size()).isEqualTo(3);
        userService.deleteById(3L);
        assertThat(userService.getAll().size()).isEqualTo(2);
    }

    void addData() {
        userService.create(new UserDto("Mikhael", "Arh@yandex.ru"));
        userService.create(new UserDto("Denis", "redis@yandex.ru"));
        userService.create(new UserDto("Alex", "lex@ya.ru"));
    }
}