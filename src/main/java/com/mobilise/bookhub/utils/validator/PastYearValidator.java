package com.mobilise.bookhub.utils.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PastYearValidator implements ConstraintValidator<PastYear, Integer> {

    @Override
    public boolean isValid(Integer publicationYear, ConstraintValidatorContext context) {
        if (publicationYear == null) {
            return true;
        }
        return publicationYear <= LocalDate.now().getYear();
    }
}
