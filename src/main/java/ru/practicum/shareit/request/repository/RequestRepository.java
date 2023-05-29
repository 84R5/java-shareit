package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;

import java.util.Collection;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> findAllByRequester_Id(Long userId);

    @Query(" select i from ItemRequest i "+
    "where i.requester.id <> ?1 "+
    "order by i.timeCreate")
    Collection<Request> findAllRequestExceptRequester(Long userId);
}
