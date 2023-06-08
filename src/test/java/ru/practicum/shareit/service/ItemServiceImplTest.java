package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;

    Request itemRequest1;
    UserDto ownerDto1;
    User owner1;
    UserDto requesterDto101;
    User requester101;
    UserDto bookerDto;
    User booker;
    UserDto userDtoForTest;
    User userForTest;
    LocalDateTime now;
    LocalDateTime nowPlus10min;
    LocalDateTime nowPlus10hours;
    Item item1;
    ItemDto itemDto1;
    RequestDto itemRequestDto1;
    CommentDto commentDto;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        nowPlus10min = now.plusMinutes(10);
        nowPlus10hours = now.plusHours(10);

        ownerDto1 = UserDto.builder()
                .name("name ownerDto1")
                .email("ownerDto1@mans.gf")
                .build();

        owner1 = User.builder()
                .id(ownerDto1.getId())
                .name(ownerDto1.getName())
                .email(ownerDto1.getEmail())
                .build();

        requesterDto101 = UserDto.builder()
                .name("name requesterDto101")
                .email("requesterDto101@mans.gf")
                .build();

        requester101 = User.builder()
                .id(requesterDto101.getId())
                .name(requesterDto101.getName())
                .email(requesterDto101.getEmail())
                .build();

        userDtoForTest = UserDto.builder()
                .name("name userDtoForTest")
                .email("userDtoForTest@userDtoForTest.zx")
                .build();

        userForTest = User.builder()
                .name(userDtoForTest.getName())
                .email(userDtoForTest.getEmail())
                .build();

        bookerDto = UserDto.builder()
                .name("booker")
                .email("booker@wa.dzd")
                .build();

        booker = User.builder()
                .name(bookerDto.getName())
                .email(bookerDto.getEmail())
                .build();

        itemRequest1 = Request.builder()
                .description("description for request 1")
                .requester(requester101)
                .created(now)
                .build();

        item1 = Item.builder()
                .name("name for item 1")
                .description("description for item 1")
                .owner(owner1)
                .available(true)
                .build();

        itemDto1 = ItemDto.builder()
                .name(item1.getName())
                .description(item1.getDescription())
                .available(item1.getAvailable())
                .build();

        itemRequestDto1 = RequestDto.builder()
                .description(item1.getDescription())
                .requester(UserMapper.toUserDto(requester101))
                .created(now)
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .created(now)
                .text("comment 1")
                .authorName(userForTest.getName())
                .build();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create_whenUserNotFound_returnNotFoundRecordInDb() {
        assertThrows(ObjectNotFoundException.class, () -> itemService.create(10000L, itemDto1));
    }


    @Test
    void getItemsByUserId_whenUserNotFoundInBD_returnException() {
        assertThrows(ObjectNotFoundException.class, () -> itemService.getItemById(1000L));
    }

    @Test
    void createComment_whenItemNotFound_thenReturnNotFoundRecordInDb() {
        UserDto savedUser1 = userService.create(ownerDto1);
        UserDto savedUser2 = userService.create(userDtoForTest);
        CommentDto commentDto = CommentDto.builder()
                .authorName(savedUser2.getName())
                .text("comment from user 1")
                .created(now)
                .build();
        Long notFoundItemId = 1001L;
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> itemService.createComment(savedUser1.getId(), notFoundItemId, commentDto));
        assertEquals(String.format("Item %d not found.",
                notFoundItemId), ex.getMessage());
    }

    @Test
    void createComment_whenUserNotFound_thenReturnNotFoundRecordInBD() {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("comment 1")
                .authorName("name user for test 2")
                .created(now.minusDays(5))
                .build();

        assertThrows(ObjectNotFoundException.class, () -> itemService.createComment(1000L, 1L, commentDto));
    }

}