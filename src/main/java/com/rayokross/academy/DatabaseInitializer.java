package com.rayokross.academy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rayokross.academy.models.Course;
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

            User student = new User("Student", "Demo", "student@rayokross.com",
                    passwordEncoder.encode("1234"),
                    "USER");
            userRepository.save(student);

            User admin = new User("Admin", "Master", "admin@rayokross.com",
                    passwordEncoder.encode("adminpass"),
                    "USER", "ADMIN");
            userRepository.save(admin);

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

            log.info(" Database initialized with test users and 3 courses.");
        } else {
            log.info(" Database already contains data. Skipping initialization.");
        }
    }
}
