package com.ThaiHoc.indentity_service.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {}
)
public @interface DobConstraint {
    String message() default "Invalid day of birth";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}


