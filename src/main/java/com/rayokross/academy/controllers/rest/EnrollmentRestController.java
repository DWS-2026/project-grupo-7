package com.rayokross.academy.controllers.rest;

import java.net.URI;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rayokross.academy.dtos.EnrollmentDTO;
import com.rayokross.academy.mappers.EnrollmentMapper;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.services.EnrollmentService;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentRestController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @PostMapping("/{courseId}")
    public ResponseEntity<EnrollmentDTO> enrollCurrentUser(@PathVariable Long courseId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Enrollment enrollment = enrollmentService.enrollUser(principal.getName(), courseId);
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/courses/{id}/player")
                    .buildAndExpand(courseId)
                    .toUri();

            return ResponseEntity.created(location).body(enrollmentMapper.toDTO(enrollment));

        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya está matriculado en este curso");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}