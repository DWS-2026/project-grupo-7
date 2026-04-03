package com.rayokross.academy.controllers;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.UserService;

@Controller
public class AdminCourseController {

    private static final Logger log = LoggerFactory.getLogger(AdminCourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentService enrollmentService;

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
            log.warn("Admin failed to create course: No image provided.");
            redirectAttributes.addFlashAttribute("errorImage", "Error: You have to upload a photo for the course.");
            return "redirect:/admin";
        }

        String contentType = imageFile.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            log.warn("Admin failed to create course: Invalid image format ({}).", contentType);
            redirectAttributes.addFlashAttribute("errorImage", "Security error: JPEG, PNG or GIF required.");
            return "redirect:/admin";
        }

        if (imageFile.getSize() > 5 * 1024 * 1024) {
            log.warn("Admin failed to create course: Image size exceeds 5MB limit.");
            redirectAttributes.addFlashAttribute("errorImage", "Error: Image size can´t exceed 5MB.");
            return "redirect:/admin";
        }

        if (course.getPrice() < 0) {

            log.warn("Admin failed to create course: Price of the course can't be negative.");
            return "redirect:/admin";

        }

        courseService.save(course, imageFile);
        log.info("Admin successfully created a new course: '{}'", course.getTitle());

        return "redirect:/admin";
    }

    @GetMapping("/admin/courses/{id}/users")
    public String showCourseUsers(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        List<User> enrolledUsers = new ArrayList<>();

        for (Enrollment e : course.getEnrollments()) {
            enrolledUsers.add(e.getUser());
        }

        model.addAttribute("course", course);
        model.addAttribute("enrolledUsers", enrolledUsers);

        return "course_users";
    }

    @PostMapping("/admin/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, Model model) {
        courseService.delete(id);
        log.info("Admin deleted course with ID: {}", id);
        return "redirect:/admin";

    }

    @GetMapping("/admin/courses/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Course> course = courseService.findById(id);

        if (course.isPresent()) {
            Course courseObj = course.get();
            model.addAttribute("course", courseObj);

            if (courseObj.getLevel() != null) {
                model.addAttribute("isBeginner", courseObj.getLevel().equals("Beginner"));
                model.addAttribute("isIntermediate", courseObj.getLevel().equals("Intermediate"));
                model.addAttribute("isAdvanced", courseObj.getLevel().equals("Advanced"));
            }

            model.addAttribute("lesson", new Lesson());

            return "edit_course";
        } else {
            return "redirect:/admin?error=notfound";
        }
    }

    @PostMapping("/admin/courses/{id}/edit")
    public String processEditCourse(@PathVariable Long id, Course updatedCourse,
            @RequestParam("imageFile") MultipartFile imageFile, Model model) {

        Course existingCourse = courseService.findById(id).orElseThrow();

        existingCourse.setTitle(updatedCourse.getTitle());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setPrice(updatedCourse.getPrice());
        existingCourse.setLevel(updatedCourse.getLevel());

        try {
            if (!imageFile.isEmpty()) {
                Blob imageBlob = new SerialBlob(imageFile.getBytes());
                existingCourse.setImage(imageBlob);
                log.debug("Image updated for course ID: {}", id);
            }
        } catch (Exception e) {
        }

        if (existingCourse.getPrice() < 0) {

            model.addAttribute("course", existingCourse);
            model.addAttribute("lesson", new Lesson());
            model.addAttribute("negativePrice", true);
            log.warn("Admin failed to create course: Price of the course can't be negative.");
            return "edit_course";

        }

        courseService.save(existingCourse);
        log.info("Admin successfully updated course ID: {}", id);

        return "redirect:/admin";
    }

    @PostMapping("/admin/courses/{courseId}/users/{userId}/remove")
    public String removeUserFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {

        Course course = courseService.findById(courseId).orElseThrow();
        User user = userService.findById(userId).orElseThrow();

        enrollmentService.removeEnrollment(user, course);

        return "redirect:/admin/courses/" + courseId + "/users";
    }
}