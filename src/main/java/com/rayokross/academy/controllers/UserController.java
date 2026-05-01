package com.rayokross.academy.controllers;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.models.User;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.UserService;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/profile")
    public String showProfile(
            @RequestParam(required = false) Boolean profileSuccess,
            @RequestParam(required = false) String error,
            Model model,
            Principal principal) {

        if (principal == null)
            return "redirect:/login";

        Optional<User> userOptional = userService.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", user.getRoles().contains("ADMIN"));
            model.addAttribute("enrollments", user.getEnrollments());

            if (profileSuccess != null && profileSuccess)
                model.addAttribute("profileSuccess", true);
            if ("last_name_required".equals(error))
                model.addAttribute("lastNameError", true);
            else if ("invalid_name".equals(error))
                model.addAttribute("invalidNameError", true);
            else if ("invalid_format".equals(error))
                model.addAttribute("invalidFormatError", true);

            model.addAttribute("pageTitle", "My Profile");
            return "profile";
        }
        return "redirect:/login";
    }

    @PostMapping("/profile/upload-photo")
    public String uploadPhoto(@RequestParam MultipartFile photo, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        try {
            userService.updateProfilePhoto(principal.getName(), photo);
            return "redirect:/profile";
        } catch (IllegalArgumentException e) {
            return "redirect:/profile?error=" + e.getMessage(); // devuelve "invalid_format"
        } catch (IOException e) {
            log.error("Error uploading photo", e);
            return "redirect:/profile?error=internal_error";
        }
    }

    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam String fullName, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        try {
            userService.updateUserProfile(principal.getName(), fullName);
            return "redirect:/profile?profileSuccess=true";
        } catch (IllegalArgumentException e) {
            return "redirect:/profile?error=" + e.getMessage();
        }
    }

    @GetMapping("/user/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);

        if (user.isPresent() && user.get().getProfilePhoto() != null) {
            try {
                Blob image = user.get().getProfilePhoto();
                Resource file = new InputStreamResource(image.getBinaryStream());
                MediaType mediaType = MediaTypeFactory.getMediaType(file).orElse(MediaType.IMAGE_JPEG);
                return ResponseEntity.ok().contentType(mediaType).body(file);
            } catch (SQLException e) {
                log.error("Failed to load profile photo for user ID {}: {}", id, e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or image not found");
    }

    @PostMapping("/profile/courses/{courseId}/cancel")
    public String cancelEnrollment(@PathVariable Long courseId, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        try {
            enrollmentService.cancelUserEnrollment(principal.getName(), courseId);
        } catch (IllegalArgumentException e) {
            log.warn("Enrollment cancellation failed: {}", e.getMessage());
        }
        return "redirect:/profile";
    }
}