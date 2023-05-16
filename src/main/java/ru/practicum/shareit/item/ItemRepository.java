package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long ownerId);

    @Query(" SELECT I FROM ITEM I" +
            "WHERE UPPER(I.NAME) LIKE UPPER(CONTACT('%', ?1, '%')) " +
            "OR UPPER (I.DESCRIPTION) LIKE UPPER9CONTACT('%', ?1, '%')) " +
            "AND I.AVAILABLE = TRUE")
    List<Item> search(String text);
}