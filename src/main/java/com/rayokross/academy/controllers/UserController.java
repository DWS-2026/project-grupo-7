package com.rayokross.academy.controllers;

import java.security.Principal;
import java.sql.Blob;
import java.util.List;
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

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.UserService;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/gif",
            "image/avif", "image/webp");

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/profile")
    public String showProfile(
            @RequestParam(required = false) Boolean profileSuccess,
            @RequestParam(required = false) String error,
            Model model,
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
            if ("last_name_required".equals(error)) {
                model.addAttribute("lastNameError", true);
            } else if ("invalid_name".equals(error)) {
                model.addAttribute("invalidNameError", true);
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

            String contentType = photo.getContentType();
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
                log.warn("User '{}' attempted to upload invalid file type: {}", principal.getName(), contentType);
                return "redirect:/profile?error=invalid_format";
            }

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

        String cleanFullName;

        if (fullName != null) {
            cleanFullName = fullName.trim();
        } else {
            cleanFullName = "";
        }

        if (cleanFullName.isEmpty() || cleanFullName.length() > 100) {
            log.warn("Invalid name submitted by user: {}", principal.getName());
            return "redirect:/profile?error=invalid_name";
        }

        if (cleanFullName.contains("<") || cleanFullName.contains(">")) {
            log.warn("HTML characters detected in name by user: {}", principal.getName());
            return "redirect:/profile?error=invalid_name";
        }

        String[] names = cleanFullName.split(" ", 2);

        if (names.length < 2 || names[1].trim().isEmpty()) {
            log.warn("User {} tried to save without a last name.", principal.getName());
            return "redirect:/profile?error=last_name_required";
        }

        Optional<User> userOpt = userService.findByEmail(principal.getName());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            user.setFirstName(names[0]);
            user.setLastName(names[1].trim());

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
        Optional<Course> courseOpt = courseService.findById(courseId);

        if (userOpt.isPresent() && courseOpt.isPresent()) {
            User user = userOpt.get();
            Course course = courseOpt.get();

            enrollmentService.removeEnrollment(user, course);

            log.info("User '{}' cancelled enrollment for course ID {}.", user.getEmail(), courseId);
        } else {
            log.warn("Enrollment cancellation failed for course ID {}.", courseId);
        }

        return "redirect:/profile";
    }
}