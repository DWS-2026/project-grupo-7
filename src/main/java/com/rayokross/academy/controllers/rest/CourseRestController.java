package com.rayokross.academy.controllers.rest;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rayokross.academy.dtos.CourseBasicDTO;
import com.rayokross.academy.dtos.CourseDetailDTO;
import com.rayokross.academy.mappers.CourseMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseRestController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping
    public ResponseEntity<Page<CourseBasicDTO>> getCatalog(
            @RequestParam(required = false) String level,
            Pageable pageable) {

        Page<Course> courses;
        if (level != null && !level.isEmpty()) {
            courses = courseService.findByLevel(level, pageable);
        } else {
            courses = courseService.findAll(pageable);
        }

        return ResponseEntity.ok(courses.map(courseMapper::toBasicDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailDTO> getCourse(@PathVariable Long id) {
        Course course = courseService.findById(id).orElseThrow();
        return ResponseEntity.ok(courseMapper.toDetailDTO(course));
    }

    @PostMapping("/{id}/enrollments")
    public ResponseEntity<Void> enrollInCourse(
            @PathVariable Long id,
            java.security.Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }

        try {
            enrollmentService.enrollUser(principal.getName(), id);
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            // El curso o el usuario no existe
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            // El usuario ya está matriculado (Conflict)
            return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{id}/media")
    public ResponseEntity<Resource> getCourseImage(@PathVariable Long id) throws SQLException {
        Course course = courseService.findById(id).orElseThrow();

        if (course.getImage() == null) {
            throw new NoSuchElementException();
        }

        Resource file = new InputStreamResource(course.getImage().getBinaryStream());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }

    @GetMapping("/{id}/syllabus")
    public ResponseEntity<Resource> downloadSyllabus(@PathVariable Long id) {
        Course course = courseService.findById(id).orElseThrow();
        Resource resource = courseService.loadSyllabusAsResource(course);

        if (resource == null) {
            throw new NoSuchElementException();
        }

        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}