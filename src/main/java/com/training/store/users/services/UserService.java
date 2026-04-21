package com.training.store.users.services;

import com.training.store.common.enums.Role;
import com.training.store.common.exceptions.BadRequestException;
import com.training.store.common.exceptions.ErrorType;
import com.training.store.common.exceptions.ResourceNotFoundException;
import com.training.store.users.dtos.ChangePasswordRequest;
import com.training.store.users.dtos.RegisterUserRequest;
import com.training.store.users.dtos.UpdateUserRequest;
import com.training.store.users.dtos.UserDto;
import com.training.store.users.mappers.UserMapper;
import com.training.store.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers(String sortBy) {
        if (!Set.of("name", "email").contains(sortBy)) {
            sortBy = "name";
        }

        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(Long userId) {

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException(ErrorType.USER_NOT_FOUND);
        }

        return userMapper.toDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(ErrorType.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        var newUser = userMapper.toEntity(request);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(Role.USER);
        userRepository.save(newUser);

        return userMapper.toDto(newUser);
    }

    public UserDto updateUser(Long userId, UpdateUserRequest request) {

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException(ErrorType.USER_NOT_FOUND);
        }

        userMapper.updateUser(request, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException(ErrorType.USER_NOT_FOUND);
        }

        userRepository.deleteById(userId);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException(ErrorType.USER_NOT_FOUND);
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new BadRequestException(ErrorType.UNAUTHORIZED_ACCESS, "Email is already registered.");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
}
