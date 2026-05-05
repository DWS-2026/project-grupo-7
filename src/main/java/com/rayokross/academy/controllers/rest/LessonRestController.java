package com.rayokross.academy.controllers.rest;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rayokross.academy.dtos.LessonDTO;
import com.rayokross.academy.mappers.LessonMapper;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.services.LessonService;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/lessons")
public class LessonRestController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonMapper lessonMapper;

    @PostMapping("/")
    public ResponseEntity<LessonDTO> addLesson(
            @PathVariable Long courseId,
            @RequestBody LessonDTO lessonDTO) {

        Lesson lesson = lessonMapper.toEntity(lessonDTO);

        Lesson savedLesson = lessonService.addLessonToCourse(courseId, lesson);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedLesson.getId())
                .toUri();

        return ResponseEntity.created(location).body(lessonMapper.toDTO(savedLesson));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable Long courseId,
            @PathVariable Long id) {

        lessonService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}