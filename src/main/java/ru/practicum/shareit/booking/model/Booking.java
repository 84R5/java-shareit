package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@Table(name = "BOOKINGS", schema = "PUBLIC")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "START_TIME")
    private LocalDateTime start;

    @Column(name = "END_TIME")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "BOOKER_ID")
    private User booker;

    @Enumerated(EnumType.STRING)
    private Status status;
}
