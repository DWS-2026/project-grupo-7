package com.rayokross.academy.controllers.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.LessonService;
import com.rayokross.academy.services.UserService;

@Controller
public class CoursePlayerController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private UserService userService;

    @GetMapping("/learn/course/{courseId}")
    public String viewCourse(@PathVariable Long courseId,
            @RequestParam(required = false) Long lessonId,
            Model model,
            Principal principal) {

        if (principal == null)
            return "redirect:/login";

        User currentUser = userService.findByEmail(principal.getName()).orElse(null);
        if (currentUser == null)
            return "redirect:/login";

        Optional<Course> courseOpt = courseService.findById(courseId);
        if (courseOpt.isEmpty())
            return "redirect:/courses";
        Course course = courseOpt.get();

        Optional<Enrollment> enrollmentOpt = enrollmentService.findByUserAndCourse(currentUser, course);
        if (enrollmentOpt.isEmpty())
            return "redirect:/courses";

        Enrollment enrollment = enrollmentOpt.get();

        Lesson currentLesson = null;
        if (lessonId != null) {
            currentLesson = lessonService.findById(lessonId).orElse(null);
        } else if (course.getLessons() != null && !course.getLessons().isEmpty()) {
            currentLesson = course.getLessons().get(0);
        }

        List<Map<String, Object>> displayLessons = new ArrayList<>();
        if (course.getLessons() != null) {
            for (Lesson lesson : course.getLessons()) {
                Map<String, Object> lessonMap = new HashMap<>();
                lessonMap.put("id", lesson.getId());
                lessonMap.put("title", lesson.getTitle());
                lessonMap.put("durationInMinutes", lesson.getDurationInMinutes());

                boolean isActive = (currentLesson != null && lesson.getId().equals(currentLesson.getId()));
                lessonMap.put("isActive", isActive);

                displayLessons.add(lessonMap);
            }
        }

        model.addAttribute("pageTitle", "Learning: " + course.getTitle());
        model.addAttribute("course", course);
        model.addAttribute("displayLessons", displayLessons);
        model.addAttribute("isCompleted", enrollment.isCompleted());
        model.addAttribute("isEnrolled", true);

        model.addAttribute("user", currentUser);
        model.addAttribute("logged", true);
        model.addAttribute("admin", currentUser.getRoles().contains("ADMIN"));

        if (currentLesson != null) {
            model.addAttribute("currentLesson", currentLesson);
            boolean hasVideo = currentLesson.getVideoUrl() != null && !currentLesson.getVideoUrl().trim().isEmpty();
            model.addAttribute("hasVideo", hasVideo);
            model.addAttribute("hasLessons", true);
        } else {
            model.addAttribute("hasLessons", false);
        }

        return "course-player";
    }

    @PostMapping("/learn/course/{courseId}/complete")
    public String completeCourse(@PathVariable Long courseId, Principal principal) {
        if (principal != null) {
            try {
                enrollmentService.setCourseCompletion(principal.getName(), courseId, true);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return "redirect:/learn/course/" + courseId;
    }

    @PostMapping("/learn/course/{courseId}/uncomplete")
    public String uncompleteCourse(@PathVariable Long courseId, Principal principal) {
        if (principal != null) {
            try {
                enrollmentService.setCourseCompletion(principal.getName(), courseId, false);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return "redirect:/learn/course/" + courseId;
    }
}