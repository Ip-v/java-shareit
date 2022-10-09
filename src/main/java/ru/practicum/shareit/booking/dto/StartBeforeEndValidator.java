package ru.practicum.shareit.booking.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingDto> {

    @Override
    public boolean isValid(final BookingDto b , final ConstraintValidatorContext context) {
        return b.getStart().isBefore(b.getEnd());
    }
}
