package com.training.store.users.controllers;

import com.training.store.users.dtos.ChangePasswordRequest;
import com.training.store.users.dtos.RegisterUserRequest;
import com.training.store.users.dtos.UpdateUserRequest;
import com.training.store.users.dtos.UserDto;
import com.training.store.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Fetch all users, with optional sorting by name or email.")
    public List<UserDto> getAllUsers(
            @RequestParam(defaultValue = "", required = false, name = "sort")
            String sortBy
    ) {

        return userService.getAllUsers(sortBy);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single user.", description = "Fetch a user using their ID")
    public UserDto getUser(@PathVariable(name = "id") Long userId) {

        return userService.getUser(userId);
    }

    @PostMapping
    @Operation(summary = "Register/Signup a new user.")
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var userDto = userService.registerUser(request);
        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update user information",
            description = "Update user information (name, email) by specifying the user ID."
    )
    public UserDto updateUser(
            @PathVariable(name = "id") Long userId,
            @RequestBody UpdateUserRequest request
            ) {

        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by specifying the user ID. This is a hard delete and cannot be undone."
    )
    public void deleteUser(@PathVariable(name = "id") Long userId) {

        userService.deleteUser(userId);
    }

    @PostMapping("/{id}/change-password")
    @Operation(
            summary = "Change user password",
            description = "Updates the password for a user identified by their user ID. The current password must be provided."
    )
    public void changePassword(
            @PathVariable(name = "id") Long userId,
            @RequestBody ChangePasswordRequest request
            ) {

        userService.changePassword(userId, request);
    }
}
