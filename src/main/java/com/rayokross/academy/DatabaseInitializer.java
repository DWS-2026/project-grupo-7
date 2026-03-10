package com.rayokross.academy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.models.User;
import com.rayokross.academy.repositories.UserRepository;
import com.rayokross.academy.services.CourseService;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

        private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

        @Autowired
        private CourseService courseService;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;

        @PostConstruct
        public void init() {
                if (courseService.findAll().isEmpty()) {
                        // Usuarios
                        userRepository.save(new User("Admin", "Master", "admin@rayokross.com",
                                        passwordEncoder.encode("adminpass1234"), "USER", "ADMIN"));
                        userRepository.save(new User("Student", "Demo", "student@rayokross.com",
                                        passwordEncoder.encode("student1234"), "USER"));

                        // Curso 1 - Con Lecciones
                        Course c1 = new Course("Ethical Hacking Pro", "Master penetration testing techniques.",
                                        "Offensive",
                                        129.99, "RayoKross Team", 45);
                        c1.setRating(4.9);
                        c1.setReviewCount(150); // Para que no salga (0 ratings)

                        // Añadimos lecciones reales
                        c1.getLessons().add(
                                        new Lesson("Introduction to Hacking", "Content...", "https://v.com/1", 10, c1));
                        c1.getLessons().add(
                                        new Lesson("Setting up Kali Linux", "Content...", "https://v.com/2", 20, c1));

                        courseService.save(c1);

                        // Curso 2
                        Course c2 = new Course("Network Defense", "Learn to defend networks.", "Defensive", 89.00,
                                        "RayoKross Team", 30);
                        c2.setRating(4.7);
                        courseService.save(c2);

                        // Curso 3
                        Course c3 = new Course("Cybersecurity Basics", "The start of your career.", "Foundations", 0.00,
                                        "URJC", 10);
                        c3.setRating(4.5);
                        courseService.save(c3);

                        log.info("Database initialized with courses and lessons.");
                }
        }
}