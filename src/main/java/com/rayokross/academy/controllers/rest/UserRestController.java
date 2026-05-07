package com.rayokross.academy.controllers.rest;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.dtos.EnrollmentBasicDTO;
import com.rayokross.academy.dtos.UserBasicDTO;
import com.rayokross.academy.dtos.UserDTO;
import com.rayokross.academy.mappers.EnrollmentMapper;
import com.rayokross.academy.mappers.UserMapper;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.UserService;

@RestController
@RequestMapping("/api/v1/users/me")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @GetMapping
    public ResponseEntity<UserDTO> getMyProfile(Principal principal) {
        if (principal == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        User user = userService.findByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @GetMapping("/enrollments")
    public ResponseEntity<List<EnrollmentBasicDTO>> getMyEnrollments(Principal principal) {
        if (principal == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        List<Enrollment> enrollmentList = enrollmentService.getMyEnrollments(principal.getName());

        return ResponseEntity.ok(enrollmentMapper.toBasicDTOs(enrollmentList));
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateMyProfile(
            @RequestBody UserBasicDTO dto,
            Principal principal) {

        userService.updateUserProfile(principal.getName(), dto.fullname());

        User updatedUser = userService.findByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(userMapper.toDTO(updatedUser));
    }

    @PutMapping("/media")
    public ResponseEntity<Void> uploadPhoto(@RequestParam MultipartFile photo, Principal principal)
            throws IOException, SQLException {

        if (photo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Photo file is empty");
        }

        userService.updateProfilePhoto(principal.getName(), photo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/media")
    public ResponseEntity<Resource> downloadImage(Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElseThrow();

        try {
            if (user.getProfilePhoto() == null) {
                throw new NoSuchElementException();
            }

            Resource file = new InputStreamResource(user.getProfilePhoto().getBinaryStream());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(file);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading image data");
        }
    }

    @DeleteMapping("/enrollments/{courseId}")
    public ResponseEntity<Void> cancelEnrollment(@PathVariable Long courseId, Principal principal) {
        try {
            enrollmentService.cancelUserEnrollment(principal.getName(), courseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new NoSuchElementException();
        }
    }
}