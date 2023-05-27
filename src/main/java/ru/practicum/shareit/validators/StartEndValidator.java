package ru.practicum.shareit.validators;

import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class StartEndValidator implements ConstraintValidator<StartEndDate, BookingRequestDto> {

    @Override
    public boolean isValid(BookingRequestDto bookingRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        return bookingRequestDto.getStart().isBefore(bookingRequestDto.getEnd()) &&
                !bookingRequestDto.getStart().equals(bookingRequestDto.getEnd());
    }
}
