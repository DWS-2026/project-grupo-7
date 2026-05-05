package com.rayokross.academy.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rayokross.academy.dtos.LessonDTO;
import com.rayokross.academy.models.Lesson;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonDTO toDTO(Lesson lesson); 

    List<LessonDTO> toDTOs(List<Lesson> lessons);

    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "course", ignore = true) 
    Lesson toEntity(LessonDTO lessonDTO);

}
