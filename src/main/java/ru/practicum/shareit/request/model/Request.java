package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Builder
@Table(name = "REQUESTS", schema = "PUBLIC")
public class Request {

    @Id
    @Column(name = "REQUEST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "REQUESTER_ID", referencedColumnName = "USER_ID")
    private User requester;

    private LocalDateTime timeCreate;

    @ManyToMany(mappedBy = "requestId")
    List<Item> items;
}
