package com.rayokross.academy.controllers.rest;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.dtos.EnrollmentDTO;
import com.rayokross.academy.dtos.EnrollmentBasicDTO;
import com.rayokross.academy.dtos.UserDTO;
import com.rayokross.academy.mappers.EnrollmentMapper;
import com.rayokross.academy.mappers.UserMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentRestController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/{courseId}")
    public ResponseEntity<EnrollmentDTO> enrollCurrentUser(@PathVariable Long courseId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Enrollment enrollment = enrollmentService.enrollUser(principal.getName(), courseId);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
            return ResponseEntity.created(location).body(enrollmentMapper.toDTO(enrollment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Page<EnrollmentBasicDTO>> getMyEnrollments(
            @PageableDefault(size = 5, page = 0) Pageable pageable,
            Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Page<Enrollment> enrollmentPage = enrollmentService.getMyEnrollments(principal.getName(), pageable);
        return ResponseEntity.ok(enrollmentPage.map(enrollmentMapper::toBasicDTO));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> cancelEnrollment(@PathVariable Long courseId, Principal principal) {
        try {
            enrollmentService.cancelUserEnrollment(principal.getName(), courseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    @PatchMapping("/{courseId}/status")
    public ResponseEntity<Void> updateEnrollmentStatus(
            @PathVariable Long courseId,
            @RequestBody Map<String, Boolean> body,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Boolean completed = body.get("completed");
        if (completed == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'completed' is required");
        }
        try {
            enrollmentService.setCourseCompletion(principal.getName(), courseId, completed);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}/users")
    public ResponseEntity<List<UserDTO>> getEnrolledUsers(@PathVariable Long courseId) {
        Course course = courseService.findById(courseId).orElseThrow();
        List<User> users = enrollmentService.getEnrolledUsers(course);
        return ResponseEntity.ok(userMapper.toDTOs(users));
    }

    @DeleteMapping("/course/{courseId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        try {
            enrollmentService.removeEnrollmentByIds(userId, courseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new NoSuchElementException();
        }
    }
}
