package com.rayokross.academy.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.UserService;

@Controller
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("allUsers", userService.findAll());
        return "admin_dashboard";
    }

    @PostMapping("/admin/courses/new")
    public String createCourse(Course course, @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) throws Exception {

        if (imageFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Error: Debes subir una imagen para el curso.");
            return "redirect:/admin";
        }

        String contentType = imageFile.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            redirectAttributes.addFlashAttribute("error",
                    "Error de seguridad: Solo se permiten archivos JPEG, PNG o GIF.");
            return "redirect:/admin";
        }

        if (imageFile.getSize() > 5 * 1024 * 1024) {
            redirectAttributes.addFlashAttribute("error", "Error: La imagen no puede superar los 5MB.");
            return "redirect:/admin";
        }

        courseService.save(course, imageFile);

        return "redirect:/admin";
    }

    @GetMapping("/admin/courses/{id}/users")
    public String showCourseUsers(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        List<User> enrolledUsers = course.getEnrollments().stream()
                .map(enrollment -> enrollment.getUser())
                .collect(Collectors.toList());

        model.addAttribute("course", course);
        model.addAttribute("enrolledUsers", enrolledUsers);

        return "course_users";
    }
}