package com.training.store.users.mappers;

import com.training.store.users.dtos.RegisterUserRequest;
import com.training.store.users.dtos.UpdateUserRequest;
import com.training.store.users.dtos.UserDto;
import com.training.store.users.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}
