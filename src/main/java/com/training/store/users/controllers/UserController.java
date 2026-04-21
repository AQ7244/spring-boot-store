package com.training.store.users.controllers;

import com.training.store.users.dtos.ChangePasswordRequest;
import com.training.store.users.dtos.RegisterUserRequest;
import com.training.store.users.dtos.UpdateUserRequest;
import com.training.store.users.dtos.UserDto;
import com.training.store.users.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(
            @RequestParam(defaultValue = "", required = false, name = "sort")
            String sortBy
    ) {

        return userService.getAllUsers(sortBy);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable(name = "id") Long userId) {

        return userService.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var userDto = userService.registerUser(request);
        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable(name = "id") Long userId,
            @RequestBody UpdateUserRequest request
            ) {

        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(name = "id") Long userId) {

        userService.deleteUser(userId);
    }

    @PostMapping("/{id}/change-password")
    public void changePassword(
            @PathVariable(name = "id") Long userId,
            @RequestBody ChangePasswordRequest request
            ) {

        userService.changePassword(userId, request);
    }
}
