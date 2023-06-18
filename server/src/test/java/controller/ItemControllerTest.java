package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@RequiredArgsConstructor
class ItemControllerTest {

    private static final String HEADER = "X-Sharer-User-Id";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    public static String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void getItemsByUser() throws Exception {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        UserDto userDto = UserDto.builder()
                .id(userId)
                .name("Lex")
                .email("a@ya.ru")
                .build();

        List<ItemDtoFull> items = Arrays.asList(
                ItemDtoFull.builder()
                        .id(1L)
                        .name("First")
                        .description("item 1")
                        .available(true)
                        .owner(userDto)
                        .build(),
                ItemDtoFull.builder()
                        .id(2L)
                        .name("Second")
                        .description("item 2")
                        .available(true)
                        .owner(userDto)
                        .build());

        when(itemService.getItemsByOwner(userId, from, size)).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header(HEADER, userId)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("First"))
                .andExpect(jsonPath("$[0].description").value("item 1"))
                .andExpect(jsonPath("$[1].name").value("Second"))
                .andExpect(jsonPath("$[1].description").value("item 2"));
    }

    @Test
    void findItemById() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        UserDto userDto = UserDto.builder()
                .id(userId)
                .name("Lex")
                .email("a@ya.ru")
                .build();
        ItemDtoFull item = ItemDtoFull.builder()
                .id(itemId)
                .name("First")
                .description("item 1")
                .available(true)
                .owner(userDto)
                .build();

        when(itemService.getItemByIdFromUser(userId, itemId)).thenReturn(item);

        mockMvc.perform(get("/items/1")
                        .header(HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("First"));
    }

    @Test
    void searchItems() throws Exception {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        UserDto userDto = new UserDto(1L, "Lex", "a@ya.ru");
        List<ItemDto> items = Arrays.asList(
                ItemDto.builder()
                        .id(1L)
                        .name("First")
                        .description("item 1")
                        .available(true)
                        .owner(userDto)
                        .build(),
                ItemDto.builder()
                        .id(2L)
                        .name("Second")
                        .description("item 2")
                        .available(true)
                        .owner(userDto)
                        .build());
        when(itemService.getItemsBySearch(userId, "item", from, size)).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .header(HEADER, userId)
                        .param("text", "item")
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("First"))
                .andExpect(jsonPath("$[0].description").value("item 1"))
                .andExpect(jsonPath("$[1].name").value("Second"))
                .andExpect(jsonPath("$[1].description").value("item 2"));
    }

    @Test
    void addItem() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        UserDto userDto = UserDto.builder()
                .id(userId)
                .name("Lex")
                .email("a@ya.ru")
                .build();
        ItemDto item = ItemDto.builder()
                .id(itemId)
                .name("First")
                .description("item 1")
                .available(true)
                .owner(userDto)
                .build();

        when(itemService.create(userId, item)).thenReturn(item);

        mockMvc.perform(post("/items")
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(item)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("First"));
    }

    @Test
    void updateItem() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        UserDto userDto = UserDto.builder()
                .id(userId)
                .name("Lex")
                .email("a@ya.ru")
                .build();
        ItemDto item1 = ItemDto.builder()
                .id(1L)
                .name("First")
                .description("item 1")
                .available(true)
                .owner(userDto)
                .build();
        ItemDto item2 = ItemDto.builder()
                .id(2L)
                .name("Second")
                .description("item 2")
                .available(true)
                .owner(userDto)
                .build();

        when(itemService.create(userId, item1)).thenReturn(item1);
        when(itemService.update(userId, itemId, item2)).thenReturn(item2);

        mockMvc.perform(post("/items")
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(item1)))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/items/1")
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(item2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Second"));
    }

    @Test
    void addComment() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        UserDto userDto = UserDto.builder()
                .id(userId)
                .name("Lex")
                .email("a@ya.ru")
                .build();
        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("First")
                .description("item 1")
                .available(true)
                .owner(userDto)
                .build();
        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("First comment")
                .itemDto(item)
                .authorName("Max")
                .build();

        when(itemService.createComment(userId, itemId, comment)).thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(comment)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("First comment"));
    }
}
