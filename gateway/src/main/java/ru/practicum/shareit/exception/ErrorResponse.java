package ru.practicum.shareit.exception;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class ErrorResponse {
    String error;
}