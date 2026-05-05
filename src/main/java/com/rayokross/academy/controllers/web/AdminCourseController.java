package com.rayokross.academy.controllers.web;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("allUsers", userService.findAll());
        return "admin_dashboard";
    }

    @PostMapping("/admin/courses/new")
    public String createCourse(
            Course course,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("syllabusFile") MultipartFile syllabusFile,
            RedirectAttributes redirectAttributes) {

        try {
            courseService.createCourse(course, imageFile, syllabusFile);
            return "redirect:/admin";
        } catch (IllegalArgumentException e) {
            log.warn("Admin failed to create course: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorImage", e.getMessage());
            return "redirect:/admin";
        } catch (IOException e) {
            log.error("IO Error saving files for course", e);
            redirectAttributes.addFlashAttribute("errorImage", "Internal error saving files.");
            return "redirect:/admin";
        }
    }

    @GetMapping("/admin/courses/{id}/users")
    public String showCourseUsers(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        List<User> enrolledUsers = enrollmentService.getEnrolledUsers(course);

        model.addAttribute("course", course);
        model.addAttribute("enrolledUsers", enrolledUsers);

        return "course_users";
    }

    @PostMapping("/admin/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/courses/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Course> courseOpt = courseService.findById(id);

        if (courseOpt.isPresent()) {
            Course courseObj = courseOpt.get();
            model.addAttribute("course", courseObj);

            if (courseObj.getLevel() != null) {
                model.addAttribute("isFoundations", courseObj.getLevel().equalsIgnoreCase("Foundations"));
                model.addAttribute("isDefensive", courseObj.getLevel().equalsIgnoreCase("Defensive"));
                model.addAttribute("isOffensive", courseObj.getLevel().equalsIgnoreCase("Offensive"));
            }
            model.addAttribute("lesson", new Lesson());

            return "edit_course";
        } else {
            return "redirect:/admin?error=notfound";
        }
    }

    @PostMapping("/admin/courses/{id}/edit")
    public String processEditCourse(
            @PathVariable Long id,
            Course updatedCourse,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("syllabusFile") MultipartFile syllabusFile,
            Model model) {

        try {
            courseService.updateCourse(id, updatedCourse, imageFile, syllabusFile);
            return "redirect:/admin";
        } catch (IllegalArgumentException e) {
            log.warn("Admin failed to edit course {}: {}", id, e.getMessage());

            Optional<Course> existingCourse = courseService.findById(id);
            if (existingCourse.isPresent()) {
                model.addAttribute("course", existingCourse.get());
            } else {
                model.addAttribute("course", updatedCourse);
            }
            model.addAttribute("lesson", new Lesson());
            model.addAttribute("negativePrice", true);

            return "edit_course";
        } catch (IOException e) {
            log.error("IO Error updating image for course ID " + id, e);
            return "redirect:/admin";
        }
    }

    @PostMapping("/admin/courses/{courseId}/users/{userId}/remove")
    public String removeUserFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {

        Course course = courseService.findById(courseId).orElseThrow();
        User user = userService.findById(userId).orElseThrow();

        enrollmentService.removeEnrollment(user, course);

        return "redirect:/admin/courses/" + courseId + "/users";
    }
}