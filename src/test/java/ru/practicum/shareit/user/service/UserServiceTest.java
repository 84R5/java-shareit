package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

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

    @DisplayName("Test create user")
    @ParameterizedTest
    @CsvSource({"Mary, blood_mary@ny.com",
            "Lola, lola_chester@gb.com"})
    void createUser(String name, String email) {
        assertThat(userService.create(new UserDto(name, email)))
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("email", email);
    }

    @Test
    void getAll_returnAllUsers_added3Users() {
        addData();
        assertThat(userService.getAll(), hasSize(3));
    }

    @Test
    void update_returnUpdatedUser_added3Users() {
        addData();
        assertThat(userService.getById(2L))
                .hasFieldOrPropertyWithValue("name", "Denis")
                .hasFieldOrPropertyWithValue("email", "redis@yandex.ru");
        assertThat(userService.update(new UserDto("Mc", "dudu@yandex.ru"), 2L))
                .hasFieldOrPropertyWithValue("email", "dudu@yandex.ru")
                .hasFieldOrPropertyWithValue("name", "Mc")
                .hasFieldOrPropertyWithValue("id", 2L);
        assertThat(userService.getById(2L))
                .hasFieldOrPropertyWithValue("name", "Mc")
                .hasFieldOrPropertyWithValue("email", "dudu@yandex.ru");
    }

    @Test
    void delete_return2Users_added3Users() {
        addData();
        assertThat(userService.getAll(), hasSize(3));
        userService.deleteById(3L);
        assertThat(userService.getAll(), hasSize(2));
    }

    void addData() {
        userService.create(new UserDto("Mikhael", "Arh@yandex.ru"));
        userService.create(new UserDto("Denis", "redis@yandex.ru"));
        userService.create(new UserDto("Alex", "lex@ya.ru"));
    }
}