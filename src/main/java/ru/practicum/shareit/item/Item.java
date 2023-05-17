package ru.practicum.shareit.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "ITEMS", schema = "PUBLIC")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private boolean available;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;
}
