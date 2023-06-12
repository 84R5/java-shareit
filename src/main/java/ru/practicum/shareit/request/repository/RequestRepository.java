package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Page<Request> findAll(Pageable pageable);

    Page<Request> findByRequesterIdNot(Long userId, Pageable pageable);

    List<Request> findAllByRequesterIdOrderByCreatedAsc(Long userId);

    List<Request> findByRequesterIdNot(Long userId);
}
