package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoToCreate;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final Clock clock;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public RequestDtoToCreate create(long userId, RequestDto requestDto) {
        return RequestMapper.toItemRequestDtoToCreate(
                requestRepository.save(Request.builder()
                        .requester(userRepository.findById(userId).orElseThrow(() ->
                                new EntityNotFoundException(String.format("User %d is not found.", userId))))
                        .id(requestDto.getId())
                        .description(requestDto.getDescription())
                        .timeCreate(LocalDateTime.now(clock))
                        .build()));
    }

    @Override
    public RequestDto getRequestById(long userId, long requestId) {
        userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("User %d is not found.", userId)));
        return RequestMapper.toItemRequestDto(requestRepository.findById(requestId)
                .orElseThrow((EntityNotFoundException::new)));
    }

    @Override
    public Collection<RequestDto> getRequestByUserId(long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("User %d is not found.", userId)));
        return RequestMapper.arrayToItemRequestDto(requestRepository.findAllByRequester_Id(userId));
    }

    @Override
    public Collection<RequestDto> getRequestAll(long userId, int from, int size) {
        userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("User %d is not found.", userId)));
        return RequestMapper.arrayToItemRequestDto(requestRepository.findAllRequestExceptRequester(userId)
                .stream().skip(from).limit(size).collect(Collectors.toList()));
    }


}
