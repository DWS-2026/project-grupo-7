package com.rayokross.academy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.UserService;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

        private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

        @Autowired
        private CourseService courseService;

        @Autowired
        private UserService userService;

        @Autowired
        private EnrollmentService enrollmentService;

        private void setCourseImage(Course course, String imageName) {
                try {
                        org.springframework.core.io.Resource resource = new org.springframework.core.io.ClassPathResource(
                                        "static/images/" + imageName);
                        if (resource.exists()) {
                                java.io.InputStream is = resource.getInputStream();
                                course.setImage(new javax.sql.rowset.serial.SerialBlob(is.readAllBytes()));
                        } else {
                                log.warn("No se pudo encontrar la imagen: " + imageName
                                                + ". Revisando en static/images/");
                        }
                } catch (Exception e) {
                        log.error("Error al cargar la imagen", e);
                }
        }

        @PostConstruct
        public void init() {

                User student = null;

                if (userService.findAll().isEmpty()) {

                        userService.save(new User("Admin", "Master", "admin@rayokross.com",
                                        "adminpass1234", "USER", "ADMIN"));

                        student = new User("Student", "Demo", "student@rayokross.com",
                                        "student1234", "USER");
                        userService.save(student);

                        log.info("Database initialized with default users.");
                } else {

                        student = userService.findByEmail("student@rayokross.com").orElse(null);
                }

                if (courseService.findAll().isEmpty()) {

                        Course c1 = new Course("Ethical Hacking Pro", "Master penetration testing techniques.",
                                        "Offensive", 129.99, "RayoKross Team", 45);
                        c1.setRating(4.9);
                        c1.setReviewCount(150);

                        c1.getLessons().add(
                                        new Lesson("Introduction to Hacking",
                                                        "<p>Welcome to the first lesson of Ethical Hacking. In this video, we will cover the basics.</p>",
                                                        "https://www.youtube.com/embed/3Kq1MIfIGCE", 10, c1));
                        c1.getLessons().add(
                                        new Lesson("Setting up Kali Linux",
                                                        "<p>Let's install Kali Linux in a Virtual Machine.</p>",
                                                        "https://www.youtube.com/embed/qZpGgL22G1k", 20, c1));

                        setCourseImage(c1, "ethical_hacking.png");
                        courseService.save(c1);

                        if (student != null) {
                                Enrollment enrollment = new Enrollment(student, c1);
                                enrollmentService.save(enrollment);
                                log.info("Test enrollment created: Student -> Ethical Hacking Pro");
                        }

                        Course c2 = new Course("Network Defense", "Learn to defend networks.", "Defensive", 89.00,
                                        "RayoKross Team", 30);
                        c2.setRating(4.7);
                        setCourseImage(c2, "Introduction_To_Secure_Networks.png");
                        courseService.save(c2);

                        Course c3 = new Course("Cybersecurity Basics", "The start of your career.", "Foundations", 0.00,
                                        "URJC", 10);
                        c3.setRating(4.5);
                        setCourseImage(c3, "forensics.png");
                        courseService.save(c3);

                        Course c4 = new Course("Web App Penetration Testing",
                                        "Learn to exploit vulnerabilities like SQLi, XSS and CSRF.", "Offensive", 95.00,
                                        "RayoKross Team",
                                        35);
                        c4.setRating(4.8);
                        setCourseImage(c4, "penetrationTesting.png");
                        c4.setReviewCount(85);
                        courseService.save(c4);

                        Course c5 = new Course("Incident Response & Forensics",
                                        "Handle breaches and perform digital forensics on Windows systems.",
                                        "Defensive", 110.00,
                                        "SecOps Lead", 40);
                        c5.setRating(4.6);
                        setCourseImage(c5, "incidentResponse.png");
                        courseService.save(c5);

                        Course c6 = new Course("Cloud Security Architecture",
                                        "Secure AWS and Azure environments from external threats.", "Defensive", 140.00,
                                        "CloudNinja", 60);
                        c6.setRating(4.7);
                        setCourseImage(c6, "cloudSecurity.png");
                        courseService.save(c6);

                        Course c7 = new Course("Advanced Malware Analysis",
                                        "Reverse engineering and dynamic analysis of modern malware.", "Offensive",
                                        150.00, "DarkCoder",
                                        50);
                        c7.setRating(4.9);
                        courseService.save(c7);

                        Course c8 = new Course("SIEM Operations with Splunk",
                                        "Detect anomalies and build security dashboards.",
                                        "Defensive", 85.00, "URJC", 25);
                        c8.setRating(4.5);
                        courseService.save(c8);

                        Course c9 = new Course("Wireless Network Hacking",
                                        "Crack WPA2/WPA3 and audit enterprise Wi-Fi networks.",
                                        "Offensive", 75.50, "RayoKross Team", 20);
                        c9.setRating(4.8);
                        courseService.save(c9);

                        log.info("Database initialized with courses and lessons.");
                }
        }
}