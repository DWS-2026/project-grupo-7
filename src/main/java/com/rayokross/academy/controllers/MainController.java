package com.rayokross.academy.controllers;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CourseService;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    public String showIndex(Model model) {
        Pageable topThree = PageRequest.of(0, 3);
        model.addAttribute("popularCourses", courseService.findAll(topThree).getContent());
        model.addAttribute("pageTitle", "Home");
        return "index";
    }

    @GetMapping("/courses")
    public String showCatalog(
            @RequestParam(required = false) String level,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        int pageSize = 6;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Course> coursePage;

        if (level != null && !level.isEmpty()) {
            coursePage = courseService.findByLevel(level, pageable);

            model.addAttribute("currentLevel", level);
            if ("Foundations".equalsIgnoreCase(level)) {
                model.addAttribute("isFoundations", true);
            }
            if ("Offensive".equalsIgnoreCase(level)) {
                model.addAttribute("isOffensive", true);
            }
            if ("Defensive".equalsIgnoreCase(level)) {
                model.addAttribute("isDefensive", true);
            }

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

    @GetMapping("/course/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) {
        Optional<Course> op = courseService.findById(id);
        if (op.isPresent() && op.get().getImage() != null) {
            try {
                Blob image = op.get().getImage();
                Resource imageFile = new InputStreamResource(image.getBinaryStream());
                MediaType mediaType = MediaTypeFactory.getMediaType(imageFile).orElse(MediaType.IMAGE_JPEG);
                return ResponseEntity.ok().contentType(mediaType).body(imageFile);
            } catch (SQLException e) {
                log.error("Failed to load image for course ID {}: {}", id, e.getMessage(), e);
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
