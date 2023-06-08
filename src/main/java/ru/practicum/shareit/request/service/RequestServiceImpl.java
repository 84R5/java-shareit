package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestServiceImpl implements RequestService {

    UserService userService;

    RequestRepository requestRepository;

    ItemRepository itemRepository;

    @Override
    public RequestDto create(Long userId, RequestDto requestDto) {
        LocalDateTime now = LocalDateTime.now(Clock.systemDefaultZone());
        if (requestDto.getDescription() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong description.");
        }
        UserDto requesterDto = userService.getUserById(userId);
        requestDto.setRequester(requesterDto);
        requestDto.setCreated(now);
        Request request = requestRepository.save(RequestMapper.toItemRequest(requestDto));
        return RequestMapper.toItemRequestDto(request);
    }

    @Override
    public List<RequestDto> getAllOwnRequests(Long userId) {
        userService.getUserById(userId);
        return requestRepository.findAllByRequesterIdOrderByCreatedAsc(userId).stream()
                .map(RequestMapper::toItemRequestDto)
                .peek(request -> {
                    List<ItemDto> items = itemRepository.findAllByRequestId(request.getId()).stream()
                            .map(ItemMapper::toItemDto)
                            .collect(Collectors.toList());
                    request.setItems(items);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getAllRequestsOthersUser(Long userId, Integer from, Integer size) {
        if (from != null && size != null) {
            if (from < 0 || size <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong request.");
            }
            int pageNumber = (int) Math.ceil((double) from / size);
            Pageable pageable = PageRequest.of(pageNumber, size);
            return requestRepository.findByRequesterIdNot(userId, pageable).stream()
                    .map(RequestMapper::toItemRequestDto)
                    .peek(request -> {
                        List<ItemDto> items = itemRepository.findAllByRequestId(request.getId()).stream()
                                .map(ItemMapper::toItemDto)
                                .collect(Collectors.toList());
                        request.setItems(items);
                    })
                    .collect(Collectors.toList());
        }
        return requestRepository.findByRequesterIdNot(userId).stream()
                .map(RequestMapper::toItemRequestDto)
                .peek(request -> {
                    List<ItemDto> items = itemRepository.findAllByRequestId(request.getId()).stream()
                            .map(ItemMapper::toItemDto)
                            .collect(Collectors.toList());
                    request.setItems(items);
                })
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto getItemRequestById(Long userId, Long requestId) {
        userService.getUserById(userId);
        RequestDto requestDto = RequestMapper.toItemRequestDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Request %s not found.", requestId))));
        List<ItemDto> listItemDto = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        requestDto.setItems(listItemDto);
        return requestDto;
    }
}

/*
package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public RequestDto create(Long userId, RequestDto requestDto) {
        if (requestDto.getDescription() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong description.");
        }
        UserDto requesterDto = userService.getUserById(userId);
        requestDto.setRequester(requesterDto);
        requestDto.setTimeCreation(LocalDateTime.now(Clock.systemDefaultZone()));
        Request request = requestRepository.save(RequestMapper.toItemRequest(requestDto));
        return RequestMapper.toItemRequestDto(request);
    }

    @Override
    public RequestDto getRequestById(Long userId, Long requestId) {
        userService.getUserById(userId);
        RequestDto itemRequestDto = RequestMapper.toItemRequestDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Request %s not found.", requestId))));
        itemRequestDto.setItems(itemRepository.findAllByRequestId(requestId));
        return itemRequestDto;
    }

    @Override
    public Collection<RequestDto> getRequestByUserId(Long userId) {
        userService.getUserById(userId);
        return requestRepository.findAllByRequesterIdOrderByCreatedAsc(userId).stream()
                .map(RequestMapper::toItemRequestDto)
                .peek(request -> {
                    request.setItems(itemRepository.findAllByRequestId(request.getId()));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Collection<RequestDto> getRequestAll(Long userId, Integer from, Integer size) {
        if (from != null && size != null) {
            if (from < 0 || size <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong request.");
            }
            int pageNumber = (int) Math.ceil((double) from / size);
            Pageable pageable = PageRequest.of(pageNumber, size);
            return requestRepository.findByRequesterIdNot(userId, pageable).stream()
                    .map(RequestMapper::toItemRequestDto)
                    .peek(request -> {
                        request.setItems(itemRepository.findAllByRequestId(request.getId()));
                    })
                    .collect(Collectors.toList());
        }
        return requestRepository.findByRequesterIdNot(userId).stream()
                .map(RequestMapper::toItemRequestDto)
                .peek(request -> {
                    request.setItems(itemRepository.findAllByRequestId(request.getId()));
                })
                .collect(Collectors.toList());
    }
}
*/
