package com.rayokross.academy.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rayokross.academy.dtos.EnrollmentDTO;
import com.rayokross.academy.models.Enrollment;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(source = "user.id", target = "userId") 
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.title", target = "courseTitle")
    EnrollmentDTO toDTO(Enrollment enrollment);
}
