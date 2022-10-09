package ru.practicum.shareit.booking;

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
    List<Booking> findByBookerIdOrderByStartDesc(Long userId);
    
    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime date);
    
    List<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus status);
    
    List<Booking> findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime date);
    
    List<Booking> findBookingsByItemIdAndEndIsBeforeOrderByEndDesc(Long id, LocalDateTime date);
    
    @Query("select b " +
            "from Booking b left join User as us on b.booker.id = us.id " +
            "where us.id = ?1 " +
            "and ?2 between b.start and b.end " +
            "order by b.start DESC")
    List<Booking> findCurrentBookingsByBooker(Long userId, LocalDateTime date);
    
    @Query("select b " +
            "from Booking b left join Item as i on b.item.id = i.id " +
            "left join User as us on i.owner.id = us.id " +
            "where us.id = ?1 " +
            "and ?2 between b.start and b.end " +
            "order by b.start DESC")
    List<Booking> findCurrentBookingsByOwner(Long userId, LocalDateTime date);
    
    List<Booking> searchBookingByItemOwnerId(Long id);
    
    List<Booking> searchBookingByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime date);
    
    List<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long id);
    
    List<Booking> findBookingsByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime date);
    
    
    List<Booking> searchBookingByBookerIdAndItemIdAndEndIsBefore(Long id, Long itemId, LocalDateTime date);
    
    List<Booking> findBookingsByItemIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime date);
}
