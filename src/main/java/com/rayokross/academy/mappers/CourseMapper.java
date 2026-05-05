package com.rayokross.academy.mappers;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.dtos.CourseBasicDTO;
import com.rayokross.academy.dtos.CourseDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = { LessonMapper.class })
public interface CourseMapper {

    CourseBasicDTO toBasicDTO(Course course);

    @Mapping(target = "syllabusFileName", ignore = true)
    CourseDetailDTO toDetailDTO(Course course);

    List<CourseBasicDTO> toBasicDTOs(List<Course> courses);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "learningObjectives", ignore = true)
    @Mapping(target = "description", ignore = true)
    Course toEntity(CourseBasicDTO dto);
}