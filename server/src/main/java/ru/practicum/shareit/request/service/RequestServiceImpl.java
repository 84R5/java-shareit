package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        UserDto requesterDto = userService.getUserById(userId);
        requestDto.setRequester(requesterDto);
        requestDto.setCreated(now);
        Request request = requestRepository.save(RequestMapper.toRequest(requestDto));
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> getAllOwnRequests(Long userId) {
        userService.getUserById(userId);
        return requestRepository.findAllByRequesterIdOrderByCreatedAsc(userId).stream()
                .map(RequestMapper::toRequestDto)
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
            int pageNumber = (int) Math.ceil((double) from / size);
            Pageable pageable = PageRequest.of(pageNumber, size);
            return requestRepository.findByRequesterIdNot(userId, pageable).stream()
                    .map(RequestMapper::toRequestDto)
                    .peek(request -> {
                        List<ItemDto> items = itemRepository.findAllByRequestId(request.getId()).stream()
                                .map(ItemMapper::toItemDto)
                                .collect(Collectors.toList());
                        request.setItems(items);
                    })
                    .collect(Collectors.toList());
        }
        return requestRepository.findByRequesterIdNot(userId).stream()
                .map(RequestMapper::toRequestDto)
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
        RequestDto requestDto = RequestMapper.toRequestDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Request %s not found.", requestId))));
        List<ItemDto> listItemDto = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        requestDto.setItems(listItemDto);
        return requestDto;
    }
}
