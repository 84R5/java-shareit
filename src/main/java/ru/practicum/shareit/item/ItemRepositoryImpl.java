
package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("ItemInMemoryRepository")
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long currentId = 0L;

    @Override
    public List<Item> findByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        if (!items.containsKey(itemId)) {
            return Optional.empty();
        }
        return Optional.of(items.get(itemId));
    }

    @Override
    public Optional<Item> create(Item item) {
        item.setId(++currentId);
        items.put(item.getId(), item);
        return Optional.of(items.get(item.getId()));
    }

    @Override
    public Optional<Item> update(Item item) {
        items.put(item.getId(), item);
        return findById(item.getId());
    }

    @Override
    public List<Item> search(String text) {
        return items.values().stream()
                .filter(i -> i.isAvailable()
                        && (i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }
}