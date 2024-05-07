package com.mobilise.bookhub.utils.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * A custom validator that checks if the provided publication year is in the past.
 *
 * @author codecharlan
 */

public class PastYearValidator implements ConstraintValidator<PastYear, Integer> {

    /**
     * Checks if the provided publication year is in the past.
     *
     * @param publicationYear The year to be validated.
     * @param context The context of the validation.
     * @return True if the publication year is in the past, false otherwise.
     */
    @Override
    public boolean isValid(Integer publicationYear, ConstraintValidatorContext context) {
        if (publicationYear == null) {
            return false;
        }
        return publicationYear <= LocalDate.now().getYear();
    }
}
