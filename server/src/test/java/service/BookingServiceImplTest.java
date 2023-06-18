package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    private BookingDto currentBookingDto;
    private BookingDto pastBookingDto;
    private BookingDto futureBookingDto;
    private BookingDto waitingBookingDto;
    private BookingDto rejectedBookingDto;
    private UserDto userDto1;
    private UserDto userDto2;
    private ItemDto itemDto1;
    private ItemDto itemDto2;


    @BeforeEach
    void beforeEach() {
        userDto1 = userService.create(new UserDto(1L, "Max", "m@mail.ru"));
        userDto2 = userService.create(new UserDto(2L, "Lex", "faf@ya.ru"));


        itemDto1 = itemService.create(userDto1.getId(),
                ItemDto.builder().name("a").description("l").available(true).build());
        itemDto2 = itemService.create(userDto2.getId(),
                ItemDto.builder().name("v").description("g").available(true).build());

        itemDto1.setComments(new ArrayList<>());
        itemDto2.setComments(new ArrayList<>());

        LocalDateTime currentStart = LocalDateTime.now().minusDays(1);
        LocalDateTime currentEnd = LocalDateTime.now().plusDays(2);
        BookingRequestDto currentBookingInputDto = BookingRequestDto.builder()
                .start(currentStart)
                .end(currentEnd)
                .itemId(itemDto1.getId())
                .build();

        LocalDateTime pastStart = LocalDateTime.now().minusDays(3);
        LocalDateTime pastEnd = LocalDateTime.now().minusDays(1);
        BookingRequestDto pastBookingInputDto = BookingRequestDto.builder()
                .start(pastStart)
                .end(pastEnd)
                .itemId(itemDto2.getId())
                .build();

        LocalDateTime futureStart = LocalDateTime.now().plusDays(1);
        LocalDateTime futureEnd = LocalDateTime.now().plusDays(3);
        BookingRequestDto futureBookingInputDto = BookingRequestDto.builder()
                .start(futureStart)
                .end(futureEnd)
                .itemId(itemDto2.getId())
                .build();

        BookingRequestDto waitingBookingInputDto = BookingRequestDto.builder()
                .start(futureStart)
                .end(futureEnd)
                .itemId(itemDto1.getId())
                .build();

        BookingRequestDto rejectedBookingInputDto = BookingRequestDto.builder()
                .start(futureStart)
                .end(futureEnd)
                .itemId(itemDto1.getId())
                .build();

        currentBookingDto = bookingService.create(userDto2.getId(), currentBookingInputDto);
        pastBookingDto = bookingService.create(userDto1.getId(), pastBookingInputDto);
        futureBookingDto = bookingService.create(userDto1.getId(), futureBookingInputDto);

        waitingBookingDto = bookingService.create(userDto2.getId(), waitingBookingInputDto);
        rejectedBookingDto = bookingService.create(userDto2.getId(), rejectedBookingInputDto);
    }

    @Test
    void create_getBookingsWhisState_addBookings() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        BookingRequestDto bookingInputDto = BookingRequestDto.builder()
                .start(start)
                .end(end)
                .itemId(itemDto2.getId())
                .build();

        BookingDto bookingDto = bookingService.create(userDto1.getId(), bookingInputDto);

        assertThat(bookingService.getBookingsWhisState(userDto1.getId(), "ALL", 0, 20)).asList()
                .contains(bookingDto);
    }

    @Test
    void getBookingsWhisState_returnBookings_addBooking() {
        assertThat(bookingService.getBookingsWhisState(userDto1.getId(), "ALL", 0, 20))
                .asList().containsExactly(futureBookingDto, pastBookingDto);

        assertThat(bookingService.getBookingsWhisState(userDto2.getId(), "ALL", 0, 20))
                .asList().containsExactly(waitingBookingDto, rejectedBookingDto, currentBookingDto);

        assertThat(bookingService.getBookingsWhisState(userDto2.getId(), "CURRENT", 0, 20))
                .asList().containsExactly(currentBookingDto);

        assertThat(bookingService.getBookingsWhisState(userDto1.getId(), "FUTURE", 0, 20))
                .asList().containsExactly(futureBookingDto);

        assertThat(bookingService.getBookingsWhisState(userDto1.getId(), "PAST", 0, 20))
                .asList().containsExactly(pastBookingDto);

        bookingService.update(userDto2.getId(), futureBookingDto.getId(), true);
        assertThat(bookingService.getBookingsWhisState(userDto1.getId(), "WAITING", 0, 20))
                .asList().containsExactly(pastBookingDto);

        bookingService.update(userDto2.getId(), pastBookingDto.getId(), false);
        pastBookingDto.setStatus("REJECTED");
        assertThat(bookingService.getBookingsWhisState(userDto1.getId(), "REJECTED", 0, 20))
                .asList().containsExactly(pastBookingDto);
        assertThat(bookingService.getBookingsWhisState(userDto2.getId(), "CANCELED", 0, 20))
                .isEqualTo(new ArrayList<>());
    }

    @Test
    void getBookingsOwner_returnBookings_addBooking() {
        assertThat(bookingService.getBookingsOwner(userDto2.getId(), "ALL", 0, 20))
                .asList().containsExactly(futureBookingDto, pastBookingDto);

        assertThat(bookingService.getBookingsOwner(userDto1.getId(), "ALL", 0, 20))
                .asList().containsExactly(waitingBookingDto, rejectedBookingDto, currentBookingDto);

        assertThat(bookingService.getBookingsOwner(userDto1.getId(), "CURRENT", 0, 20))
                .asList().containsExactly(currentBookingDto);

        assertThat(bookingService.getBookingsOwner(userDto2.getId(), "FUTURE", 0, 20))
                .asList().containsExactly(futureBookingDto);


        assertThat(bookingService.getBookingsOwner(userDto2.getId(), "PAST", 0, 20))
                .asList().containsExactly(pastBookingDto);

        bookingService.update(userDto2.getId(), futureBookingDto.getId(), true);
        assertThat(bookingService.getBookingsOwner(userDto2.getId(), "WAITING", 0, 20))
                .asList().containsExactly(pastBookingDto);
        bookingService.update(userDto2.getId(), pastBookingDto.getId(), false);
        pastBookingDto.setStatus("REJECTED");
        assertThat(bookingService.getBookingsOwner(userDto2.getId(), "REJECTED", 0, 20))
                .asList().containsExactly(pastBookingDto);
        assertThat(bookingService.getBookingsOwner(userDto2.getId(), "CANCELED", 0, 20))
                .isEqualTo(new ArrayList<>());
    }

    @Test
    void getBookingById_return1Booking_bookingIdByCurrentBooking() {
        assertThat(bookingService.getBookingById(userDto1.getId(), currentBookingDto.getId()))
                .isEqualTo(currentBookingDto);
    }

    @Test
    void update_returnUpdateAndRejected_addWaiting() {
        assertThat(bookingService.getBookingById(userDto1.getId(), currentBookingDto.getId()))
                .hasFieldOrPropertyWithValue("status", "WAITING");
        assertThat(bookingService.update(userDto1.getId(), currentBookingDto.getId(), true))
                .hasFieldOrPropertyWithValue("status", "APPROVED");

        assertThat(bookingService.getBookingById(userDto1.getId(), futureBookingDto.getId()))
                .hasFieldOrPropertyWithValue("status", "WAITING");
        assertThat(bookingService.update(userDto2.getId(), futureBookingDto.getId(), false))
                .hasFieldOrPropertyWithValue("status", "REJECTED");
    }
}
