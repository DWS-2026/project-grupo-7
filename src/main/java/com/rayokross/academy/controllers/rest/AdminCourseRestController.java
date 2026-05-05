package com.rayokross.academy.controllers.rest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/api/v1/courses") // Recurso base para cursos
public class AdminCourseRestController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    // POST: Crear nuevo curso (Solo datos, Tema 4.6 - Caso 2)
    @PostMapping("/")
    public ResponseEntity<CourseDetailDTO> createCourse(@RequestBody CourseBasicDTO courseDTO) throws IOException {
        Course course = courseMapper.toEntity(courseDTO);

        // Usamos el método del servicio que sanitiza y valida
        // Pasamos null en los archivos porque en REST se suelen subir por separado
        courseService.createCourse(course, null, null);

        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(course.getId()).toUri();

        return ResponseEntity.created(location).body(courseMapper.toDetailDTO(course));
    }

    // PUT: Actualizar curso (Tema 4.6 - Caso 3)
    @PutMapping("/{id}")
    public ResponseEntity<CourseDetailDTO> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseBasicDTO updatedDTO) throws IOException {

        return courseService.findById(id).map(existingCourse -> {
            try {
                // AÑADIMOS un 'null' extra al final para el syllabusFile
                // El servicio se encargará de sanitizar la descripción automáticamente
                courseService.updateCourse(id, courseMapper.toEntity(updatedDTO), null, null);

                return ResponseEntity.ok(courseMapper.toDetailDTO(existingCourse));
            } catch (IOException e) {
                throw new RuntimeException("Error updating course", e);
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // POST/PUT: Gestionar imagen por separado (Tema 4.6)
    @PostMapping("/{id}/media")
    public ResponseEntity<Object> uploadImage(@PathVariable Long id, @RequestParam MultipartFile imageFile)
            throws IOException, SQLException {

        if (courseService.findById(id).isPresent()) {
            courseService.updateCourseImage(id, imageFile); // Método en el servicio[cite: 1]
            return ResponseEntity.noContent().build(); // 204 No Content al subir archivo
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE: Borrar curso
    @DeleteMapping("/{id}")
    public ResponseEntity<CourseBasicDTO> deleteCourse(@PathVariable Long id) {
        return courseService.findById(id).map(course -> {
            courseService.delete(id);
            return ResponseEntity.ok(courseMapper.toBasicDTO(course));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserDTO>> getEnrolledUsers(@PathVariable Long id) {
        Optional<Course> courseOpt = courseService.findById(id);

        if (courseOpt.isPresent()) {
            List<User> users = enrollmentService.getEnrolledUsers(courseOpt.get());
            // Usamos el mapper para convertir la lista de usuarios a DTOs
            return ResponseEntity.ok(userMapper.toDTOs(users));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{courseId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        try {
            enrollmentService.removeEnrollmentByIds(userId, courseId);
            return ResponseEntity.noContent().build(); // 204 No Content[cite: 4]
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: Subir el temario (Syllabus) al disco (Requisito Práctica)
    @PostMapping("/{id}/syllabus")
    public ResponseEntity<Object> uploadSyllabus(
            @PathVariable Long id,
            @RequestParam MultipartFile syllabusFile) throws IOException {

        Optional<Course> courseOpt = courseService.findById(id);

        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            // 1. Guardamos el fichero físicamente en /uploads
            courseService.saveSyllabus(course, syllabusFile);
            // 2. Actualizamos la entidad con el nombre del fichero
            courseService.save(course);

            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.notFound().build();
    }
}