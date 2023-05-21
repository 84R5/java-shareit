package ru.practicum.shareit.comment.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder(toBuilder = true)
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
    @Column(name = "CREATED_DATE")
    private LocalDateTime created;
}
