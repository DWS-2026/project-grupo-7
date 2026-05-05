package com.rayokross.academy.controllers;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CartService;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.UserService;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CartService cartService;

    @GetMapping("/courses")
    public String showCatalog(
            @RequestParam(required = false) String level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursePage;

        if (level != null && !level.isEmpty()) {
            coursePage = courseService.findByLevel(level, pageable);
            model.addAttribute("currentLevel", level);
            model.addAttribute("isFoundations", "Foundations".equalsIgnoreCase(level));
            model.addAttribute("isOffensive", "Offensive".equalsIgnoreCase(level));
            model.addAttribute("isDefensive", "Defensive".equalsIgnoreCase(level));
            model.addAttribute("filterLevel", "&level=" + level);
        } else {
            coursePage = courseService.findAll(pageable);
            model.addAttribute("filterLevel", "");
        }

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", coursePage.hasNext());
        model.addAttribute("hasPrev", coursePage.hasPrevious());
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("prevPage", page - 1);
        model.addAttribute("pageTitle", "Course Catalog");

        return "courses";
    }

    @GetMapping("/courses/{id}")
    public String showCourseDetails(@PathVariable Long id, Model model, Principal principal) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        model.addAttribute("course", course);
        model.addAttribute("pageTitle", course.getTitle());

        boolean isEnrolled = false, isAdmin = false, isInCart = false;

        if (principal != null) {
            Optional<User> userOpt = userService.findByEmail(principal.getName());
            if (userOpt.isPresent()) {
                isAdmin = userOpt.get().getRoles().contains("ADMIN");
                isEnrolled = enrollmentService.findByUserAndCourse(userOpt.get(), course).isPresent();
            }
        }

        // REFACTOR: Delegamos al CartService la comprobación
        isInCart = cartService.isCourseInCart(course.getId());

        model.addAttribute("isEnrolled", isEnrolled);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isInCart", isInCart);
        model.addAttribute("canPurchase", !isAdmin && !isEnrolled && !isInCart);

        return "courseDescription";
    }

    @GetMapping("/courses/{id}/image")
    public ResponseEntity<Resource> downloadCourseImage(@PathVariable Long id) throws SQLException {

        Optional<Course> courseOpt = courseService.findById(id);

        if (courseOpt.isPresent() && courseOpt.get().getImage() != null) {
            Resource file = new InputStreamResource(courseOpt.get().getImage().getBinaryStream());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(file);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course image not found");
    }
}