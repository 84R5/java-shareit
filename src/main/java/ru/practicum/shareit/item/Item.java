package ru.practicum.shareit.item;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
public class Item {

    private Long id;
    private String name;
    private String description;
    private boolean available;
    private Long owner;
    private ItemRequest request;
}
