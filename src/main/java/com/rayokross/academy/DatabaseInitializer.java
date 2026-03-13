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
        if (userRepository.findAll().isEmpty()) {
            // Usuarios
            userRepository.save(new User("Admin", "Master", "admin@rayokross.com",
                    passwordEncoder.encode("adminpass1234"), "USER", "ADMIN"));
            userRepository.save(new User("Student", "Demo", "student@rayokross.com",
                    passwordEncoder.encode("student1234"), "USER"));
            log.info("Database initialized with default users.");
        }

        if (courseService.findAll().isEmpty()) {
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

            Course c4 = new Course("Web App Penetration Testing", "Learn to exploit vulnerabilities like SQLi, XSS and CSRF.", "Offensive", 95.00, "RayoKross Team", 35);
            c4.setRating(4.8);
            c4.setReviewCount(85);
            courseService.save(c4);

            Course c5 = new Course("Advanced Malware Analysis", "Reverse engineering and dynamic analysis of modern malware.", "Offensive", 150.00, "DarkCoder", 50);
            c5.setRating(4.6);
            courseService.save(c5);

            Course c6 = new Course("Wireless Network Hacking", "Crack WPA2/WPA3 and audit enterprise Wi-Fi networks.", "Offensive", 75.50, "RayoKross Team", 20);
            c6.setRating(4.7);
            courseService.save(c6);

            Course c7 = new Course("Incident Response & Forensics", "Handle breaches and perform digital forensics on Windows systems.", "Defensive", 110.00, "SecOps Lead", 40);
            c7.setRating(4.9);
            courseService.save(c7);

            Course c8 = new Course("SIEM Operations with Splunk", "Detect anomalies and build security dashboards.", "Defensive", 85.00, "URJC", 25);
            c8.setRating(4.5);
            courseService.save(c8);

            Course c9 = new Course("Cloud Security Architecture", "Secure AWS and Azure environments from external threats.", "Defensive", 140.00, "CloudNinja", 60);
            c9.setRating(4.8);
            courseService.save(c9);

            log.info("Database initialized with courses and lessons.");
        }
    }
}
