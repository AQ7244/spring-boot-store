package com.training.store.users.dtos;

import com.training.store.common.validation.constraint.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {

    private Long id;

    @NotBlank(message = "Name is required!")
    @Size(min = 3, max = 100, message = "Please enter a name between 3 and 100 characters.")
    private String name;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email must be valid")
    @Lowercase(message = "Email must be in lowercase")
    private String email;
}
