package com.training.store.common.validation.constraint;

import com.training.store.common.validation.validator.CategoryExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryExistsValidator.class)
@Documented
public @interface CategoryExists {

    String message() default "Invalid category ID: category does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
