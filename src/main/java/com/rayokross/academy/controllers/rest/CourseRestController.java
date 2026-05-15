package com.rayokross.academy.controllers.rest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.security.Principal;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import com.rayokross.academy.dtos.CourseBasicDTO;
import com.rayokross.academy.dtos.CourseDetailDTO;
import com.rayokross.academy.dtos.LessonDTO;
import com.rayokross.academy.mappers.CourseMapper;
import com.rayokross.academy.mappers.LessonMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.LessonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseRestController {

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

    @GetMapping("/{id}/media")
    public ResponseEntity<Resource> getCourseImage(@PathVariable Long id) throws SQLException {
        Course course = courseService.findById(id).orElseThrow();
        if (course.getImage() == null) {
            throw new NoSuchElementException();
        }
        Resource file = new InputStreamResource(course.getImage().getBinaryStream());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
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

    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<Page<LessonDTO>> getCourseLessons(
            @PathVariable Long courseId,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        enrollmentService.findByUserEmailAndCourse(principal.getName(), course)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enrolled in this course"));
        Page<Lesson> lessonPage = lessonService.findLessonsByCourseId(courseId, pageable);
        return ResponseEntity.ok(lessonPage.map(lessonMapper::toDTO));
    }

    @PostMapping
    public ResponseEntity<CourseDetailDTO> createCourse(@Valid @RequestBody CourseDetailDTO courseDTO) throws IOException {
        Course course = courseMapper.toEntity(courseDTO);
        course = courseService.createCourse(course, null, null);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(course.getId()).toUri();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.findById(id).orElseThrow();
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestParam MultipartFile imageFile) throws IOException, SQLException {
        courseService.findById(id).orElseThrow();
        courseService.updateCourseImage(id, imageFile);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/syllabus")
    public ResponseEntity<Void> uploadSyllabus(
            @PathVariable Long id,
            @RequestParam("syllabusFile") MultipartFile syllabusFile) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        try {
            courseService.saveSyllabus(course, syllabusFile);
            courseService.save(course);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing file to disk");
        }
    }
}
