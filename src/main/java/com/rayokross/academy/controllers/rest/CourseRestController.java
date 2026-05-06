package com.rayokross.academy.controllers.rest;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rayokross.academy.dtos.CourseBasicDTO;
import com.rayokross.academy.dtos.CourseDetailDTO;
import com.rayokross.academy.mappers.CourseMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CourseService;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseRestController {

    @Autowired
    private CourseService courseService;

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
        return courseService.findById(id)
                .map(course -> ResponseEntity.ok(courseMapper.toDetailDTO(course)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/media")
    public ResponseEntity<Resource> getCourseImage(@PathVariable Long id) throws SQLException {
        Optional<Course> courseOpt = courseService.findById(id);

        if (courseOpt.isPresent() && courseOpt.get().getImage() != null) {
            Resource file = new InputStreamResource(courseOpt.get().getImage().getBinaryStream());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(file);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/syllabus")
    public ResponseEntity<?> downloadSyllabus(@PathVariable Long id) {
        return courseService.findById(id).map(course -> {
            Resource resource = courseService.loadSyllabusAsResource(course);

            if (resource == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        }).orElse(ResponseEntity.notFound().build());
    }

}