package com.training.store.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Old password is required!")
    private String oldPassword;
    @NotBlank(message = "New password is required!")
    @Size(min = 6, max = 25, message = "Please enter a password between 6 and 25 characters.")
    private String newPassword;
}
