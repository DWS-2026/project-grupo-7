package com.rayokross.academy.controllers.rest;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.dtos.UserDTO;
import com.rayokross.academy.mappers.UserMapper;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.UserService;

@RestController
@RequestMapping("/api/v1/users") // Prefijo obligatorio
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserMapper userMapper;

    /**
     * GET /me: Devuelve el perfil del usuario autenticado (Tema 4.4).
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return userService.findByEmail(principal.getName())
                .map(user -> ResponseEntity.ok(userMapper.toDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /me: Actualiza los datos del perfil (nombre completo).
     */
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateMyProfile(@RequestParam String fullName, Principal principal) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            userService.updateUserProfile(principal.getName(), fullName);
            User updatedUser = userService.findByEmail(principal.getName()).orElseThrow();
            return ResponseEntity.ok(userMapper.toDTO(updatedUser));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * POST /me/media: Sube la foto de perfil (Tema 4.6 - Caso 2).[cite: 1, 4]
     */
    @PostMapping("/me/media")
    public ResponseEntity<Object> uploadPhoto(@RequestParam MultipartFile photo, Principal principal)
            throws IOException, SQLException {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        userService.updateProfilePhoto(principal.getName(), photo);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * GET /{id}/media: Descarga la foto de cualquier usuario (Tema 4.6).
     */
    @GetMapping("/{id}/media")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws SQLException {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getProfilePhoto() != null) {
            Resource file = new InputStreamResource(user.getProfilePhoto().getBinaryStream());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // O detección dinámica como en tus notas
                    .body(file);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /me/enrollments/{courseId}: Cancela la matrícula (Borrado de recurso).
     */
    @DeleteMapping("/me/enrollments/{courseId}")
    public ResponseEntity<Void> cancelEnrollment(@PathVariable Long courseId, Principal principal) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            enrollmentService.cancelUserEnrollment(principal.getName(), courseId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}