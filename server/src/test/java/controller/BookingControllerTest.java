package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@RequiredArgsConstructor
class BookingControllerTest {

    private static final String HEADER = "X-Sharer-User-Id";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

    public static String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void getAllBookingsWithStateTest() throws Exception {
        Long userId = 1L;
        String state = "WAITING";
        Integer from = 0;
        Integer size = 10;
        UserDto booker = UserDto.builder().id(userId).build();
        List<BookingDto> bookingList = Arrays.asList(
                BookingDto.builder()
                        .id(1L)
                        .booker(booker)
                        .item(new ItemDto())
                        .status(state)
                        .build(),
                BookingDto.builder()
                        .id(2L)
                        .booker(booker)
                        .item(new ItemDto())
                        .status(state)
                        .build());

        when(bookingService.getBookingsWhisState(userId, state, from, size)).thenReturn(bookingList);

        mockMvc.perform(get("/bookings")
                        .header(HEADER, userId)
                        .param("state", state)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].status").value("WAITING"));
    }

    @Test
    void getAllBookingByOwnerTest() throws Exception {
        Long userId = 1L;
        String state = "WAITING";
        Integer from = 0;
        Integer size = 10;
        UserDto booker = UserDto.builder().id(userId).build();
        booker.setId(userId);
        List<BookingDto> bookingList = Arrays.asList(
                BookingDto.builder()
                        .id(1L)
                        .booker(booker)
                        .item(new ItemDto())
                        .status(state).build(),
                BookingDto.builder()
                        .id(2L)
                        .booker(booker)
                        .item(new ItemDto())
                        .status(state).build());


        when(bookingService.getBookingsOwner(userId, state, from, size)).thenReturn(bookingList);

        mockMvc.perform(get("/bookings/owner")
                        .header(HEADER, userId)
                        .param("state", state)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].status").value("WAITING"));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        Long userId = 1L;
        UserDto booker = UserDto.builder().id(userId).build();
        BookingDto booking = BookingDto.builder()
                .id(1L).booker(booker)
                .item(new ItemDto())
                .status("WAITING")
                .build();

        when(bookingService.getBookingById(userId, 1L)).thenReturn(booking);

        mockMvc.perform(get("/bookings/1")
                        .header(HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void createTest() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        UserDto booker = UserDto.builder().id(userId).build();
        BookingDto booking = BookingDto.builder()
                .id(1L).booker(booker)
                .item(ItemDto.builder().id(itemId).build())
                .status("WAITING")
                .build();
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .id(1L)
                .bookerId(userId)
                .itemId(itemId)
                .status("WAITING")
                .build();

        when(bookingService.create(userId, bookingRequestDto)).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(bookingRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.booker.id").value(1));
    }

    @Test
    void updateTrue() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        boolean approved = true;
        UserDto booker = UserDto.builder().id(userId).build();
        BookingDto booking = BookingDto.builder()
                .id(bookingId)
                .booker(booker)
                .item(new ItemDto())
                .status("ACCEPTED")
                .build();

        when(bookingService.update(userId, bookingId, approved)).thenReturn(booking);

        mockMvc.perform(patch("/bookings/1").header(HEADER, userId).param("approved", String.valueOf(approved)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }
}
