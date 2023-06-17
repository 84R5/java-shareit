package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum State {
    ALL,
    WAITING,
    CURRENT,
    REJECTED,
    CANCELED,
    FUTURE,
    PAST;

    public static Optional<State> from(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
