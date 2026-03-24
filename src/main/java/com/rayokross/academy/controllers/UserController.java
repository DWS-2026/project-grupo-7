package com.rayokross.academy.controllers;

import java.security.Principal;
import java.sql.Blob;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

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
import org.springframework.web.util.HtmlUtils;

import com.rayokross.academy.models.User;
import com.rayokross.academy.services.UserService;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(@RequestParam(required = false) Boolean profileSuccess, Model model,
            Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<User> userOptional = userService.findByEmail(principal.getName());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);

            model.addAttribute("isAdmin", user.getRoles().contains("ADMIN"));
            model.addAttribute("enrollments", user.getEnrollments());
            if (profileSuccess != null && profileSuccess) {
                model.addAttribute("profileSuccess", true);
            }

            model.addAttribute("pageTitle", "My Profile");
            return "profile";
        }
        return "redirect:/login";
    }

    @PostMapping("/profile/upload-photo")
    public String uploadPhoto(@RequestParam MultipartFile photo, Principal principal) {
        if (principal == null) {
            log.warn("Photo upload failed: User not authenticated.");
            return "redirect:/login";
        }

        Optional<User> userOpt = userService.findByEmail(principal.getName());
        if (userOpt.isPresent() && !photo.isEmpty()) {
            User user = userOpt.get();

            try {
                byte[] bytes = photo.getBytes();
                Blob blob = new SerialBlob(bytes);

                user.setProfilePhoto(blob);
                userService.save(user);

                log.info("User '{}' successfully updated their profile photo.", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to process profile photo for user '{}': {}", user.getEmail(), e.getMessage(), e);
            }
        } else if (photo.isEmpty()) {
            log.debug("User '{}' attempted to upload an empty photo.", principal.getName());
        }

        return "redirect:/profile";
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

            } catch (Exception e) {
                log.error("Failed to load profile photo for user ID {}: {}", id, e.getMessage(), e);

                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error interno al procesar la imagen");
            }
        }

        log.warn("Profile photo not found for user ID {}. Triggering 404 error page.", id);

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario o imagen no encontrados");
    }

    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam String fullName, Principal principal) {
        if (principal == null) {
            log.warn("Profile edit attempt failed: User not authenticated.");
            return "redirect:/login";
        }

        if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100) {
            log.warn("Invalid name submitted by user: {}", principal.getName());
            return "redirect:/profile?error=invalid_name";
        }

        String safeFullName = HtmlUtils.htmlEscape(fullName.trim());

        Optional<User> userOpt = userService.findByEmail(principal.getName());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String[] names = safeFullName.split(" ", 2);
            user.setFirstName(names[0]);
            if (names.length > 1) {
                user.setLastName(names[1]);
            } else {
                user.setLastName("");
            }

            userService.save(user);
            log.info("User '{}' updated their profile name.", user.getEmail());
        }
        return "redirect:/profile?profileSuccess=true";
    }

    @PostMapping("/profile/courses/{courseId}/cancel")
    public String cancelEnrollment(@PathVariable Long courseId, Principal principal) {
        if (principal == null) {
            log.warn("Enrollment cancellation attempt failed: User not authenticated.");
            return "redirect:/login";
        }

        Optional<User> userOpt = userService.findByEmail(principal.getName());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            boolean isRemoved = user.getEnrollments()
                    .removeIf(enrollment -> enrollment.getCourse().getId().equals(courseId));

            if (isRemoved) {
                userService.save(user);
                log.info("User '{}' cancelled enrollment for course ID {}.", user.getEmail(), courseId);
            }
        }

        return "redirect:/profile";
    }
}
