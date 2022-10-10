package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * DTO бронирований
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@StartBeforeEnd(groups = {Create.class})
public class BookingDto {
    private Long id;
    @FutureOrPresent(groups = {Create.class})
    private LocalDateTime start;
    @Future(groups = {Create.class})
    private LocalDateTime end;
    private Long bookerId;
    private Long itemId;
    private BookingStatus status;
}
