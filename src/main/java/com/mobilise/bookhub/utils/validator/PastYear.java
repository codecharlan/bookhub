package com.mobilise.bookhub.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Annotation for validating that a field or method represents a publication year that is not in the future.
 *
 * @author codecharlan
 *
 */
@Documented
@Constraint(validatedBy = PastYearValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PastYear {

    /**
     * The error message to be returned if the annotated field or method does not meet the validation criteria.
     *
     * @return the error message
     */
    String message() default "Publication year cannot be in the future";

    /**
     * Groups that this constraint belongs to.
     *
     * @return the groups
     */
    Class<?>[] groups() default {};

    /**
     * Payloads that this constraint belongs to.
     *
     * @return the payloads
     */
    Class<? extends Payload>[] payload() default {};
}
