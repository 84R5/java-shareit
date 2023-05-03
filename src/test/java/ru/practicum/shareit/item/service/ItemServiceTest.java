package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemServiceTest {

    private final UserService userService;
    private final ItemService itemService;

    @Test
    void create() {
        addData();
        assertThat(itemService.getById(2L))
                .hasFieldOrPropertyWithValue("name", "Молоток")
                .hasFieldOrPropertyWithValue("description", "Может забивать, может не забивать");
    }

    @Test
    void update() {
        addData();
        assertThat(itemService.getById(2L))
                .hasFieldOrPropertyWithValue("name", "Молоток")
                .hasFieldOrPropertyWithValue("description", "Может забивать, может не забивать");
        itemService.update(2L, 2L, new ItemDto("Молоток, с синей изолентой", "Усовершенствованн, может пробить броню", true));
        assertThat(itemService.getById(2L))
                .hasFieldOrPropertyWithValue("name", "Молоток, с синей изолентой")
                .hasFieldOrPropertyWithValue("description", "Усовершенствованн, может пробить броню");
    }

    @Test
    void getById() {
        addData();
        assertThat(itemService.getById(3L))
                .hasFieldOrPropertyWithValue("name", "Футбольное поле")
                .hasFieldOrPropertyWithValue("description", "Для игры в футбол, требуется мяч");
    }

    @Test
    void getByUserId() {
        addData();
        assertThat(itemService.getByUserId(2L).size()).isEqualTo(2);
    }

    @Test
    void search_returnItemByText() {
        addData();
        assertThat(itemService.search("мяч").size()).isEqualTo(2);
    }

    void addData() {
        userService.create(new UserDto("Mikhael", "Arh@yandex.ru"));
        userService.create(new UserDto("Denis", "redis@yandex.ru"));
        userService.create(new UserDto("Alex", "lex@ya.ru"));

        itemService.create(1L, new ItemDto("Зонт", "Для ненамокания", true));
        itemService.create(2L, new ItemDto("Молоток", "Может забивать, может не забивать", true));
        itemService.create(3L, new ItemDto("Футбольное поле", "Для игры в футбол, требуется мяч", true));
        itemService.create(2L, new ItemDto("Мяч", "футбольныймяч", true));
        itemService.create(3L, new ItemDto("Ракета", "Для межпланетных перелётов", true));
    }
}