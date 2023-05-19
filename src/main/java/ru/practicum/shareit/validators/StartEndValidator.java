package ru.practicum.shareit.validators;

import ru.practicum.shareit.booking.dto.BookingInputDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class StartEndValidator implements ConstraintValidator<StartEndDate, BookingInputDto> {

    @Override
    public boolean isValid(BookingInputDto bookingInputDto, ConstraintValidatorContext constraintValidatorContext) {
        return bookingInputDto.getStart().isBefore(bookingInputDto.getEnd()) &&
                !bookingInputDto.getStart().equals(bookingInputDto.getEnd());
    }
}
