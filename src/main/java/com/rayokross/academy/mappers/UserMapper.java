package com.rayokross.academy.mappers;

import java.util.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rayokross.academy.dtos.UserDTO;
import com.rayokross.academy.dtos.UserRegistrationDTO;
import com.rayokross.academy.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user); // Convierte de Entidad a DTO para mostrar datos

    List<UserDTO> toDTOs(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "profilePhoto", ignore = true) // Ignora el Blob de la foto
    @Mapping(target = "enrollments", ignore = true) // Ignora la lista de matrículas[cite: 1, 4]
    User toEntity(UserRegistrationDTO dto); // Convierte de DTO a Entidad para el registro
}