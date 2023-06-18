package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@RequiredArgsConstructor
class ItemRequestControllerTest {

    static final String HEADER = "X-Sharer-User-Id";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RequestService requestService;

    public static String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void getAllOwnRequests() throws Exception {
        Long userId = 1L;
        UserDto requester = UserDto.builder().build();
        List<ItemDto> items = new ArrayList<>();
        List<RequestDto> itemRequestDtoList = Arrays.asList(
                RequestDto.builder().id(1L).description("request 1").requester(requester).items(items).build(),
                RequestDto.builder().id(2L).description("request 2").requester(requester).items(items).build());

        when(requestService.getAllOwnRequests(userId)).thenReturn(itemRequestDtoList);

        mockMvc.perform(get("/requests").header(HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("request 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("request 2"));
    }

    @Test
    void getAllOthersRequests() throws Exception {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        UserDto requester = UserDto.builder().build();
        List<ItemDto> items = new ArrayList<>();
        List<RequestDto> itemRequestDtoList = Arrays.asList(
                RequestDto.builder().id(1L).description("request 1").requester(requester).items(items).build(),
                RequestDto.builder().id(2L).description("request 2").requester(requester).items(items).build());

        when(requestService.getAllRequestsOthersUser(userId, from, size)).thenReturn(itemRequestDtoList);

        mockMvc.perform(get("/requests/all")
                        .header(HEADER, userId)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("request 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("request 2"));
    }

    @Test
    void getRequest() throws Exception {
        Long userId = 1L;
        Long requestId = 1L;
        UserDto requester = UserDto.builder().build();
        List<ItemDto> items = new ArrayList<>();
        RequestDto itemRequestDto = RequestDto.builder().id(1L).description("request 1").requester(requester).items(items).build();

        when(requestService.getItemRequestById(userId, requestId)).thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .header(HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("request 1"));
    }

    @Test
    void addRequest() throws Exception {
        Long userId = 1L;
        UserDto requester = UserDto.builder().build();
        List<ItemDto> items = new ArrayList<>();
        RequestDto itemRequestDto = RequestDto.builder().id(1L).description("request 1").requester(requester).items(items).build();

        when(requestService.create(userId, itemRequestDto)).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("request 1"));
    }
}
