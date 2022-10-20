package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий бронирований
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
//    @Query("select b from Booking b where b.booker.id = ?1")
    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);
    //List<Booking> findByBookerIdOrderByStartDesc(Long booker, PageRequest pageRequest);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime date, Pageable pageRequest);

    List<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus status,
                                                                  Pageable pageRequest);

    List<Booking> findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime date,
                                                                       Pageable pageRequest);

    List<Booking> findBookingsByItemIdAndEndIsBeforeOrderByEndDesc(Long id, LocalDateTime date);

    @Query("select b " +
            "from Booking b left join User as us on b.booker.id = us.id " +
            "where us.id = ?1 " +
            "and ?2 between b.start and b.end " +
            "order by b.start DESC")
    List<Booking> findCurrentBookingsByBooker(Long userId, LocalDateTime date, Pageable pageRequest);

    @Query("select b " +
            "from Booking b left join Item as i on b.item.id = i.id " +
            "left join User as us on i.owner.id = us.id " +
            "where us.id = ?1 " +
            "and ?2 between b.start and b.end " +
            "order by b.start DESC")
    List<Booking> findCurrentBookingsByOwner(Long userId, LocalDateTime date, Pageable pageRequest);

    List<Booking> searchBookingByItemOwnerId(Long id, Pageable pageRequest);

    List<Booking> searchBookingByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime date,
                                                                            Pageable pageRequest);

    List<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long id, Pageable pageRequest);

    List<Booking> findBookingsByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime date,
                                                                          Pageable pageRequest);


    List<Booking> searchBookingByBookerIdAndItemIdAndEndIsBefore(Long id, Long itemId, LocalDateTime date);

    List<Booking> findBookingsByItemIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime date);
}
