package com.rayokross.academy.controllers.rest;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.dtos.UserBasicDTO;
import com.rayokross.academy.dtos.UserDTO;
import com.rayokross.academy.mappers.UserMapper;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.UserService;

@RestController
@RequestMapping("/api/v1/users/me")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserDTO> getMyProfile(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(userMapper.toDTO(user));
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
    public ResponseEntity<Void> uploadPhoto(@RequestParam MultipartFile photo, Principal principal) throws IOException, SQLException {
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
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading image data");
        }
    }
}
