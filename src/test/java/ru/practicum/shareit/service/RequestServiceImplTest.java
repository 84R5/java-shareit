package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceImplTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;

    private UserDto userDto1;
    private UserDto userDto2;
    private RequestDto requestDto1;
    private RequestDto requestDto2;
    ItemDto itemDto1;
    ItemDto itemDto2;

    @BeforeEach
    void beforeEach() {
        userDto1 = userService.create(UserDto.builder().name("Leo").email("king@ya.ru").build());
        userDto2 = userService.create(UserDto.builder().name("Ann").email("princess@ya.ru").build());

        requestDto1 = requestService.create(userDto1.getId(),
                RequestDto.builder().description("req1").build());
        requestDto2 = requestService.create(userDto2.getId(),
                RequestDto.builder().description("req2").build());

        itemDto1 = itemService.create(userDto2.getId(),
                ItemDto.builder().name("i1").description("di1").available(true).requestId(requestDto2.getId()).build());
        itemDto2 = itemService.create(userDto1.getId(),
                ItemDto.builder().name("i2").description("di2").available(true).requestId(requestDto1.getId()).build());


        requestDto1.setItems(requestService.getItemRequestById(requestDto1.getRequester().getId(),
                requestDto1.getId()).getItems());
        requestDto2.setItems(requestService.getItemRequestById(requestDto2.getRequester().getId(),
                requestDto2.getId()).getItems());
    }

    @Test
    void create_findItemRequest_addRequest() {
        RequestDto requestInputDto = RequestDto.builder().description("test").build();
        RequestDto requestDto = requestService.create(userDto1.getId(), requestInputDto);

        requestDto.setItems(requestService.getItemRequestById(requestDto.getRequester().getId(),
                requestDto.getId()).getItems());

        assertThat(requestService.getItemRequestById(userDto2.getId(), requestDto.getId())).isEqualTo(requestDto);
    }


    @Test
    void getByRequesterId_return1ItemRequestEveryTime_added2ItemRequests() {
        List<RequestDto> result1 = requestService.getAllOwnRequests(userDto1.getId());
        assertThat(result1.size()).isEqualTo(1);
        assertThat(result1.get(0)).isEqualTo(requestDto1);

        List<RequestDto> result2 = requestService.getAllOwnRequests(userDto2.getId());
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result2.get(0)).isEqualTo(requestDto2);
    }
}

