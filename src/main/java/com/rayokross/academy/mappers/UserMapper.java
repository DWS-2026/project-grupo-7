package com.rayokross.academy.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rayokross.academy.dtos.UserDTO;
import com.rayokross.academy.dtos.UserRegistrationDTO;
import com.rayokross.academy.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    List<UserDTO> toDTOs(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "profilePhoto", ignore = true)
    @Mapping(target = "enrollments", ignore = true) 
    User toEntity(UserRegistrationDTO dto); 
}