package com.rayokross.academy.controllers.rest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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

import com.rayokross.academy.dtos.CourseBasicDTO;
import com.rayokross.academy.dtos.CourseDetailDTO;
import com.rayokross.academy.dtos.UserDTO;
import com.rayokross.academy.mappers.CourseMapper;
import com.rayokross.academy.mappers.UserMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;

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
    public ResponseEntity<CourseDetailDTO> createCourse(@RequestBody CourseBasicDTO courseDTO) throws IOException {
        Course course = courseMapper.toEntity(courseDTO);

        course = courseService.createCourse(course, null, null);

        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(course.getId()).toUri();

        return ResponseEntity.created(location).body(courseMapper.toDetailDTO(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDetailDTO> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseBasicDTO updatedDTO) {

        return courseService.findById(id).map(existingCourse -> {
            try {
                Course updated = courseService.updateCourse(id, courseMapper.toEntity(updatedDTO), null, null);
                return ResponseEntity.ok(courseMapper.toDetailDTO(updated));
            } catch (IOException e) {
                throw new RuntimeException("Error updating course", e);
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/media")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestParam MultipartFile imageFile)
            throws IOException, SQLException {

        if (courseService.findById(id).isPresent()) {
            courseService.updateCourseImage(id, imageFile);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (courseService.findById(id).isPresent()) {
            courseService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserDTO>> getEnrolledUsers(@PathVariable Long id) {
        Optional<Course> courseOpt = courseService.findById(id);

        if (courseOpt.isPresent()) {
            List<User> users = enrollmentService.getEnrolledUsers(courseOpt.get());
            return ResponseEntity.ok(userMapper.toDTOs(users));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{courseId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        try {
            enrollmentService.removeEnrollmentByIds(userId, courseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/syllabus")
    public ResponseEntity<Void> uploadSyllabus(
            @PathVariable Long id,
            @RequestParam MultipartFile syllabusFile) throws IOException {

        Optional<Course> courseOpt = courseService.findById(id);

        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            courseService.saveSyllabus(course, syllabusFile);
            courseService.save(course);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}