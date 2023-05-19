package ru.practicum.shareit.comment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "COMMENTS", schema = "PUBLIC")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    private LocalDateTime created;
}
