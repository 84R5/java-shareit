package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp between b.start and b.end " +
            "order by b.start desc ")
    List<Booking> findAllByBookerIdAndStateCurrentOrderByStartDesc(Long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp > b.end " +
            "order by b.start desc ")
    List<Booking> findAllByBookerIdAndStatePastOrderByStartDesc(Long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and current_timestamp < b.start " +
            "order by b.start desc ")
    List<Booking> findAllByBookerIdAndStateFutureOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status);


    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            " order by b.start desc ")
    List<Booking> findAllByOwnerIdOrderByStartDesc(Long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp between b.start and b.end " +
            "order by b.start desc ")
    List<Booking> findAllByOwnerIdAndStateCurrentOrderByStartDesc(Long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp > b.end " +
            "order by b.start desc ")
    List<Booking> findAllByOwnerIdAndStatePastOrderByStartDesc(Long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and current_timestamp < b.start " +
            "order by b.start desc ")
    List<Booking> findAllByOwnerIdAndStateFutureOrderByStartDesc(Long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.status = ?2 " +
            " order by b.start desc ")
    List<Booking> findAllByOwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(Long itemId,
                                                                             LocalDateTime localDate,
                                                                             Status status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc(Long itemId,
                                                                           LocalDateTime localDate,
                                                                           Status status);

    Boolean existsByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime dateTime);
}