package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceImplTest {
    private final RequestService itemRequestService;
    private final UserService userService;
    private final EntityManager em;


    Request itemRequest1;
    UserDto ownerDto1;
    UserDto requesterDto101;
    User owner1;
    User requester101;
    LocalDateTime now;
    LocalDateTime nowPlus10min;
    LocalDateTime nowPlus10hours;
    Item item1;
    RequestDto itemRequestDto1;
    TypedQuery<Request> query;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        nowPlus10min = now.plusMinutes(10);
        nowPlus10hours = now.plusHours(10);

        ownerDto1 = UserDto.builder()
                .name("name userDto1")
                .email("userDto1@mans.gf")
                .build();
        requesterDto101 = UserDto.builder()
                .name("name userDto2")
                .email("userDto2@mans.gf")
                .build();

        owner1 = User.builder()
                .id(ownerDto1.getId())
                .name(ownerDto1.getName())
                .email(ownerDto1.getEmail())
                .build();

        requester101 = User.builder()
                .id(requesterDto101.getId())
                .name(requesterDto101.getName())
                .email(requesterDto101.getEmail())
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

        itemRequestDto1 = RequestDto.builder()
                .description(item1.getDescription())
                .requester(UserMapper.toUserDto(requester101))
                .created(now)
                .build();
    }

    @Test
    void addRequest() {
        UserDto savedOwnerDto1 = userService.create(ownerDto1);
        query =
                em.createQuery("Select ir from Request ir", Request.class);
        List<Request> beforeSave = query.getResultList();

        assertEquals(0, beforeSave.size());

        RequestDto savedRequest =
                itemRequestService.create(savedOwnerDto1.getId(), itemRequestDto1);
        List<Request> afterSave = query.getResultList();

        assertEquals(1, afterSave.size());
        assertEquals(savedRequest.getId(), afterSave.get(0).getId());
        assertEquals(savedRequest.getCreated(), afterSave.get(0).getCreated());
        assertEquals(savedRequest.getDescription(), afterSave.get(0).getDescription());
    }

    @Test
    void addRequest_whenRequesterIdIsNull() {
        Long requesterId = 1001L;
        assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.create(requesterId, itemRequestDto1));
    }

    @Test
    void addRequest_whenRequesterNotFound() {
        Long requesterId = 1001L;
        assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.create(requesterId, itemRequestDto1));
    }

    @Test
    void getRequestsByUserId() {
        UserDto savedUserDto = userService.create(requesterDto101);
        RequestDto savedRequest =
                itemRequestService.create(savedUserDto.getId(), itemRequestDto1);

        query = em.createQuery("Select ir from Request ir", Request.class);

        List<RequestDto> itemsFromDb =
                itemRequestService.getAllOwnRequests(savedUserDto.getId());

        assertEquals(1, itemsFromDb.size());

        assertEquals(savedRequest.getId(), itemsFromDb.get(0).getId());
        assertEquals(savedRequest.getRequester().getId(), itemsFromDb.get(0).getRequester().getId());
        assertEquals(savedRequest.getRequester().getName(), itemsFromDb.get(0).getRequester().getName());
        assertEquals(savedRequest.getCreated(), itemsFromDb.get(0).getCreated());
        assertEquals(itemRequestDto1.getDescription(), itemsFromDb.get(0).getDescription());
    }

    @Test
    void getRequestsByUserId_whenUserNotFound() {
        Long requesterId = 1001L;
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getAllOwnRequests(requesterId));
        assertEquals(String.format("User {} not found" + requesterId, requesterId), ex.getMessage());
    }

    @Test
    void getAllRequestForUser() {
        UserDto savedRequesterDto = userService.create(requesterDto101);
        UserDto savedOwnerDto = userService.create(ownerDto1);

        RequestDto savedRequest =
                itemRequestService.create(savedRequesterDto.getId(), itemRequestDto1);
        query = em.createQuery("Select ir from Request ir where ir.requester.id <> :userId", Request.class);

        List<RequestDto> emptyItemsFromDbForRequester =
                itemRequestService.getAllRequestsOthersUser(savedRequesterDto.getId(), 0, 5);

        assertEquals(0, emptyItemsFromDbForRequester.size());

        List<RequestDto> oneItemFromDbForOwner =
                itemRequestService.getAllRequestsOthersUser(savedOwnerDto.getId(), 0, 1);

        assertEquals(savedRequest.getId(), oneItemFromDbForOwner.get(0).getId());
        assertEquals(savedRequest.getDescription(), oneItemFromDbForOwner.get(0).getDescription());
        assertEquals(savedRequest.getRequester().getId(), oneItemFromDbForOwner.get(0).getRequester().getId());
        assertEquals(savedRequest.getRequester().getName(), oneItemFromDbForOwner.get(0).getRequester().getName());
        assertEquals(Collections.emptyList(), oneItemFromDbForOwner.get(0).getItems());
        assertEquals(savedRequest.getCreated(), oneItemFromDbForOwner.get(0).getCreated());
    }

    @Test
    void getRequestById_returnRequestDto() {
        UserDto savedRequesterDto = userService.create(requesterDto101);
        UserDto savedOwnerDto = userService.create(ownerDto1);
        UserDto observer = userService.create(UserDto.builder().name("nablyudatel").email("1@re.hg").build());

        RequestDto savedItRequest =
                itemRequestService.create(savedRequesterDto.getId(), itemRequestDto1);

        RequestDto itRequestDtoFromDbObserver =
                itemRequestService.getItemRequestById(observer.getId(), savedItRequest.getId());

        assertEquals(savedItRequest.getId(), itRequestDtoFromDbObserver.getId());
        assertEquals(savedItRequest.getCreated(), itRequestDtoFromDbObserver.getCreated());
        assertEquals(savedItRequest.getDescription(), itRequestDtoFromDbObserver.getDescription());
        assertEquals(savedItRequest.getRequester().getId(), itRequestDtoFromDbObserver.getRequester().getId());
        assertEquals(savedItRequest.getRequester().getId(), itRequestDtoFromDbObserver.getRequester().getId());

        RequestDto itemRequestDtoWithAnswerForOwner =
                itemRequestService.getItemRequestById(savedOwnerDto.getId(), savedItRequest.getId());

        assertEquals(savedItRequest.getId(), itemRequestDtoWithAnswerForOwner.getId());
        assertEquals(savedItRequest.getCreated(), itemRequestDtoWithAnswerForOwner.getCreated());
        assertEquals(savedItRequest.getDescription(), itemRequestDtoWithAnswerForOwner.getDescription());
        assertEquals(savedItRequest.getRequester().getId(), itemRequestDtoWithAnswerForOwner.getRequester().getId());
        assertEquals(savedItRequest.getRequester().getId(), itemRequestDtoWithAnswerForOwner.getRequester().getId());

        RequestDto itReqDtoWithAnswerForRequester =
                itemRequestService.getItemRequestById(savedRequesterDto.getId(), savedItRequest.getId());

        assertEquals(savedItRequest.getId(), itReqDtoWithAnswerForRequester.getId());
        assertEquals(savedItRequest.getCreated(), itReqDtoWithAnswerForRequester.getCreated());
        assertEquals(savedItRequest.getDescription(), itReqDtoWithAnswerForRequester.getDescription());
        assertEquals(savedItRequest.getRequester().getId(), itReqDtoWithAnswerForRequester.getRequester().getId());
        assertEquals(savedItRequest.getRequester().getId(), itReqDtoWithAnswerForRequester.getRequester().getId());
    }

    @Test
    void getRequestById_whenRequestNotFound() {
        UserDto savedRequesterDto = userService.create(requesterDto101);
        Long requestId = 1001L;
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getItemRequestById(savedRequesterDto.getId(), requestId));
        assertEquals("Request " + requestId + " not found.",
                ex.getMessage());
    }

}