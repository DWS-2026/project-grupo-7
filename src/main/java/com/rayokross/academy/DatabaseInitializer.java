package com.rayokross.academy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CourseService;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    @Autowired
    private CourseService courseService;

    @PostConstruct
    public void init() {
        // Comprobamos si está vacía para no duplicar cursos si reinicias
        if (courseService.findAll().isEmpty()) {

            Course c1 = new Course("Ethical Hacking Pro", "Master advanced penetration testing techniques.",
                    "Offensive", 129.99, "RayoKross Team", 45);
            c1.setRating(4.9);
            courseService.save(c1);

            Course c2 = new Course("Network Defense & SIEM", "Learn to monitor and detect network threats.",
                    "Defensive", 89.00, "RayoKross Team", 30);
            c2.setRating(4.7);
            courseService.save(c2);

            Course c3 = new Course("Cybersecurity Foundations", "The essential start for digital security.",
                    "Foundations", 0.00, "URJC Experts", 10);
            c3.setRating(4.5);
            courseService.save(c3);

            System.out.println("✅ Base de datos inicializada con 3 cursos de prueba.");
        }
    }
}