package com.rayokross.academy.controllers.rest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import com.rayokross.academy.dtos.CourseDetailDTO;
import com.rayokross.academy.dtos.UserDTO;
import com.rayokross.academy.mappers.CourseMapper;
import com.rayokross.academy.mappers.UserMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/courses")
public class AdminCourseRestController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping
    public ResponseEntity<CourseDetailDTO> createCourse(@Valid @RequestBody CourseDetailDTO courseDTO)
            throws IOException {
        Course course = courseMapper.toEntity(courseDTO);
        course = courseService.createCourse(course, null, null);
        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(course.getId()).toUri();
        return ResponseEntity.created(location).body(courseMapper.toDetailDTO(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDetailDTO> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseDetailDTO updatedDTO) {

        courseService.findById(id).orElseThrow();

        try {
            Course updated = courseService.updateCourse(id, courseMapper.toEntity(updatedDTO), null, null);
            return ResponseEntity.ok(courseMapper.toDetailDTO(updated));
        } catch (IOException e) {
            throw new RuntimeException("Error updating course", e);
        }
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestParam MultipartFile imageFile)
            throws IOException, SQLException {

        courseService.findById(id).orElseThrow();

        courseService.updateCourseImage(id, imageFile);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.findById(id).orElseThrow();

        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserDTO>> getEnrolledUsers(@PathVariable Long id) {
        Course course = courseService.findById(id).orElseThrow();

        List<User> users = enrollmentService.getEnrolledUsers(course);
        return ResponseEntity.ok(userMapper.toDTOs(users));
    }

    @DeleteMapping("/{courseId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        try {
            enrollmentService.removeEnrollmentByIds(userId, courseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    @PostMapping("/{id}/syllabus")
    public ResponseEntity<Void> uploadSyllabus(
            @PathVariable Long id,
            @RequestParam("syllabusFile") MultipartFile syllabusFile) { // Añadido el nombre explícito

        Course course = courseService.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Course not found"));

        try {
            courseService.saveSyllabus(course, syllabusFile);
            courseService.save(course);
            return ResponseEntity.noContent().build();

        } catch (IOException e) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Error writing file to disk");
        }
    }
}