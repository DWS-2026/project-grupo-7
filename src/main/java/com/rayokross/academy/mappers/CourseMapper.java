package com.rayokross.academy.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rayokross.academy.dtos.CourseBasicDTO;
import com.rayokross.academy.dtos.CourseDetailDTO;
import com.rayokross.academy.models.Course;

@Mapper(componentModel = "spring", uses = { LessonMapper.class })
public interface CourseMapper {

    CourseBasicDTO toBasicDTO(Course course);

    @Mapping(target = "syllabusFileName", ignore = true)
    CourseDetailDTO toDetailDTO(Course course);

    default List<CourseBasicDTO> toBasicDTOs(List<Course> courses) {
        if (courses == null) return java.util.Collections.emptyList();
        return courses.stream().map(this::toBasicDTO).collect(java.util.stream.Collectors.toList());
    }
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "learningObjectives", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "syllabusFileName", ignore = true)
    Course toEntity(CourseBasicDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "learningObjectives", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "syllabusFileName", ignore = true)
     @Mapping(target = "creatorName", ignore = true)
    Course toEntity(CourseDetailDTO dto);
}