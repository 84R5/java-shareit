package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "REQUESTS", schema = "PUBLIC")
public class ItemRequest {

    @Id
    @Column(name = "REQUEST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "REQUESTOR_ID")
    private User requestor;

    private LocalDateTime added;
}
