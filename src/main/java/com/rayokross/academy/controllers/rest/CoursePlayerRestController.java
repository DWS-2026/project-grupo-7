package com.rayokross.academy.controllers.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.dtos.CourseProgressDTO;
import com.rayokross.academy.mappers.CourseMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;

@RestController
@RequestMapping("/api/v1/courses")
public class CoursePlayerRestController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/{courseId}/player")
    public ResponseEntity<CourseProgressDTO> getCoursePlayer(
            @PathVariable Long courseId,
            Principal principal) {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        Enrollment enrollment = enrollmentService.findByUserEmailAndCourse(principal.getName(), course)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enrolled in this course"));

        CourseProgressDTO progress = new CourseProgressDTO(
                courseMapper.toDetailDTO(course),
                enrollment.isCompleted(),
                (course.getLessons().isEmpty()) ? null : course.getLessons().get(0).getId());

        return ResponseEntity.ok(progress);
    }

    @PatchMapping("/{courseId}/completion")
    public ResponseEntity<Void> updateCompletionStatus(
            @PathVariable Long courseId,
            @RequestParam boolean status,
            Principal principal) {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            enrollmentService.setCourseCompletion(principal.getName(), courseId, status);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
