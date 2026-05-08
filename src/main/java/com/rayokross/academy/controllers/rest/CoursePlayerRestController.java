package com.rayokross.academy.controllers.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.dtos.LessonDTO;
import com.rayokross.academy.mappers.CourseMapper;
import com.rayokross.academy.mappers.LessonMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.LessonService;

@RestController
@RequestMapping("/api/v1/courses")
public class CoursePlayerRestController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private LessonMapper lessonMapper;

    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<Page<LessonDTO>> getCourseLessons(
            @PathVariable Long courseId,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            Principal principal) {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        enrollmentService.findByUserEmailAndCourse(principal.getName(), course)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enrolled in this course"));

        Page<Lesson> lessonPage = lessonService.findLessonsByCourseId(courseId, pageable);

        Page<LessonDTO> lessonDTOPage = lessonPage.map(lessonMapper::toDTO);

        return ResponseEntity.ok(lessonDTOPage);
    }

    @PatchMapping("/{courseId}/status")
    public ResponseEntity<Void> updateEnrollmentStatus(
            @PathVariable Long courseId,
            @RequestBody java.util.Map<String, Boolean> body,
            Principal principal) {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Boolean completed = body.get("completed");
        if (completed == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'completed' is required");

        try {
            enrollmentService.setCourseCompletion(principal.getName(), courseId, completed);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}