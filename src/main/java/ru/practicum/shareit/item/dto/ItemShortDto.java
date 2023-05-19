package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemShortDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;

    public ItemShortDto(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemShortDto(Long id, String name, String description, Boolean available) {
        this(name, description, available);
        this.id = id;
    }
}
