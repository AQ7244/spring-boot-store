package com.training.store.common.validation.validator;

import com.training.store.products.repositories.CategoryRepository;
import com.training.store.common.validation.constraint.CategoryExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryExistsValidator implements ConstraintValidator<CategoryExists, Byte> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public boolean isValid(Byte value, ConstraintValidatorContext context) {
        if (value == null) return true; // handled separately by @NotNull
        return categoryRepository.existsById(value);
    }
}
