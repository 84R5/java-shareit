package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.util.Pagination;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private RequestServiceImpl requestService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private Pagination pagination;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User user1;
    private Item item1;
    private Request request;
    private ItemDto itemInputDto1;
    private Comment comment;
    private CommentDto commentInputDto;


    Pageable pageable;


    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "Farad", "gods@gods.ru");
        User user2 = new User(2L, "Faradgo", "gossds@dgodssads.ru");
        request = new Request(1L, "dscrptn", user2, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        item1 = new Item(1L, "Sam", "dscrptn", true, user1, request);
        ItemDtoFull itemFullDto1 = ItemMapper.toItemDtoWithDate(item1);

        itemInputDto1 = ItemDto.builder().id(item1.getId()).name(item1.getName()).description(item1.getDescription()).available(item1.getAvailable()).build();

        comment = new Comment(1L, "dfseago", item1, user1, LocalDateTime.now());
        commentInputDto = CommentDto.builder().text(comment.getText()).build();
        pageable = PageRequest.of(1, 20);
        itemFullDto1.setComments(new ArrayList<>());
    }

    @Test
    void search_return1Item_withText() {
        when(itemRepository.searchItems("sea", pageable)).thenReturn(List.of(item1));
        when(pagination.getPage(1, 20)).thenReturn(pageable);
        List<ItemDto> exList = List.of(ItemMapper.toItemDto(item1));
        assertThat(itemService.getItemsBySearch(1L, "sea", 1, 20)).isEqualTo(exList);
    }

    @Test
    void searchItems_returnEmpty_wrongText() {
        assertThat(itemService.getItemsBySearch(1L, "", 1, 20).size()).isEqualTo(0);
        verify(itemRepository, times(0)).searchItems("", pageable);
    }

    @Test
    void getItemsBySearch_throwResponseStatusException_wrongSizeOrFrom() {
        when(pagination.getPage(0, 0)).thenThrow(ResponseStatusException.class);
        assertThrows(ResponseStatusException.class, () -> itemService.getItemsBySearch(1L, "cbcbx", 0, 0));
    }

    @Test
    void getItemById_return1Item() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        assertThat(itemService.getItemById(1L)).isEqualTo(item1);
    }

    @Test
    void getItemsByOwner_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> itemService.getItemsByOwner(999L, 1, 20));
    }

    @Test
    void getItemsByOwner_returnItem1() {
        UserDto u = UserMapper.toUserDto(user1);
        ItemDtoFull itemDtoFull = ItemMapper.toItemDtoWithDate(item1);
        when(itemRepository.findByOwner(user1, PageRequest.of(1, 10))).thenReturn(List.of(item1));
        when(userService.getUserById(1L)).thenReturn(u);

        assertThat(itemService.getItemsByOwner(1L, 1, 10)).isEqualTo(List.of(itemDtoFull));
        when(itemRepository.findByOwner(user1)).thenReturn(List.of(item1));
        assertThat(itemService.getItemsByOwner(1L,null,null)).isEqualTo(List.of((itemDtoFull)));
    }

    @Test
    void getById_throwObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> itemService.getItemById(1L));
    }

    @Test
    void create_returnItem_addItem() {
        ItemDto exItemDto = ItemDto.builder().id(item1.getId()).name(item1.getName()).description(item1.getDescription()).available(item1.getAvailable()).owner(UserMapper.toUserDto(user1)).requestId(request.getId()).build();
        when(userService.getUserById(user1.getId())).thenReturn(UserMapper.toUserDto(user1));
        when(requestService.getItemRequestById(user1.getId(), request.getId())).thenReturn(RequestMapper.toRequestDto(request));
        when(requestRepository.findById(item1.getRequest().getId())).thenReturn(Optional.of(request));
        when(itemRepository.save(item1)).thenReturn(item1);
        assertThat(itemService.create(user1.getId(), ItemMapper.toItemDto(item1))).isEqualTo(exItemDto);
    }

    @Test
    void update_returnItemDto_updateItem() {
        when(userService.getUserById(user1.getId())).thenReturn(UserMapper.toUserDto(user1));
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.ofNullable(item1));
        when(itemRepository.save(item1)).thenReturn(item1);
        ItemDto exItem = ItemDto.builder().id(itemInputDto1.getId()).name(itemInputDto1.getName()).description(itemInputDto1.getDescription()).owner(UserMapper.toUserDto(user1)).requestId(request.getId()).available(itemInputDto1.getAvailable()).build();
        assertThat(itemService.update(1L, 1L, itemInputDto1)).isEqualTo(exItem);
    }

    @Test
    void getItemById_returnItem_Item() {
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));

        assertThat(itemService.getItemById(item1.getId())).isEqualTo(item1);
    }


    @Test
    void createComment_returnComment_CommentDto() {
        Booking booking = Booking.builder().id(1L).booker(user1).status(Status.APPROVED).start(LocalDateTime.now().plusMinutes(5).truncatedTo(ChronoUnit.SECONDS)).end(LocalDateTime.now().plusMinutes(10).truncatedTo(ChronoUnit.SECONDS)).build();
        when(userService.getUserById(user1.getId())).thenReturn(UserMapper.toUserDto(user1));
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.ofNullable(item1));
        bookingRepository.save(booking);
        when(bookingRepository.findBookingByItemIdAndBookerIdAndStatusAndEndBefore(item1.getId(), user1.getId(), Status.APPROVED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))).thenReturn(List.of(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto commentDto = itemService.createComment(1L, 1L, commentInputDto);

        assertThat(commentDto.getText()).isEqualTo(comment.getText());
        assertThat(commentDto.getAuthorName()).isEqualTo(comment.getAuthor().getName());
        assertThat(commentDto.getCreated().truncatedTo(ChronoUnit.SECONDS)).isEqualTo(comment.getCreated().truncatedTo(ChronoUnit.SECONDS));
    }
}