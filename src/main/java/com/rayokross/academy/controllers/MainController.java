package com.rayokross.academy.controllers;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CourseService;

@Controller
public class MainController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("popularCourses", courseService.findAll());
        model.addAttribute("pageTitle", "Home");
        return "index";
    }

    @GetMapping("/courses")
    public String showCatalog(@RequestParam(required = false) String level, Model model) {

        if (level != null && !level.isEmpty()) {
            // Buscamos solo los cursos de esa categoría
            model.addAttribute("courses", courseService.findByLevel(level));

            // Activamos las variables para que Mustache pinte el botón de color
            model.addAttribute("currentLevel", level);
            if ("Foundations".equalsIgnoreCase(level))
                model.addAttribute("isFoundations", true);
            if ("Offensive".equalsIgnoreCase(level))
                model.addAttribute("isOffensive", true);
            if ("Defensive".equalsIgnoreCase(level))
                model.addAttribute("isDefensive", true);
        } else {
            // Si no hay filtro, mostramos todo el catálogo
            model.addAttribute("courses", courseService.findAll());
        }

        model.addAttribute("pageTitle", "Course Catalog");
        return "courses";
    }

    @GetMapping("/course/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Optional<Course> op = courseService.findById(id);
        if (op.isPresent() && op.get().getImage() != null) {
            Blob image = op.get().getImage();
            Resource imageFile = new InputStreamResource(image.getBinaryStream());
            MediaType mediaType = MediaTypeFactory.getMediaType(imageFile).orElse(MediaType.IMAGE_JPEG);
            return ResponseEntity.ok().contentType(mediaType).body(imageFile);
        }
        return ResponseEntity.notFound().build();
    }
}