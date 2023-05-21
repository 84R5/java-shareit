package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingForItem;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "ITEMS", schema = "PUBLIC")
public class Item {

    @ManyToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    List<Comment> comments;
    @With
    @Transient
    BookingForItem lastBooking;
    @With
    @Transient
    BookingForItem nextBooking;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private boolean available;
    @With
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;

}