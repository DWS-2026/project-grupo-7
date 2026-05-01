package com.rayokross.academy.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.User;
import com.rayokross.academy.repositories.EnrollmentRepository;

@Service
public class EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    public Optional<Enrollment> findByUserAndCourse(User user, Course course) {
        return enrollmentRepository.findByUserAndCourse(user, course);
    }

    public void save(Enrollment enrollment) {
        enrollmentRepository.save(enrollment);
        log.info("Enrollment saved (ID: {})", enrollment.getId());
    }

    public void removeEnrollment(User user, Course course) {
        Optional<Enrollment> enrollment = enrollmentRepository.findByUserAndCourse(user, course);
        if (enrollment.isPresent()) {
            enrollmentRepository.delete(enrollment.get());
        }
    }

    public List<User> getEnrolledUsers(Course course) {
        List<User> enrolledUsers = new ArrayList<>();
        if (course.getEnrollments() != null) {
            for (Enrollment e : course.getEnrollments()) {
                enrolledUsers.add(e.getUser());
            }
        }
        return enrolledUsers;
    }

    public void checkoutCart(String userEmail, List<Course> cartCourses) throws IllegalArgumentException {

        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (cartCourses == null || cartCourses.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        for (Course course : cartCourses) {
            // Comprobamos si ya está matriculado
            boolean alreadyEnrolled = false;
            for (Enrollment e : user.getEnrollments()) {
                if (e.getCourse().getId().equals(course.getId())) {
                    alreadyEnrolled = true;
                    break;
                }
            }

            if (!alreadyEnrolled) {
                Enrollment newEnrollment = new Enrollment(user, course);
                user.getEnrollments().add(newEnrollment);
            }
        }

        // Al guardar el usuario, por la relación en cascada de Hibernate, se guardarán
        // los enrollments
        userService.save(user);
        log.info("Checkout successful for user '{}'.", user.getEmail());
    }

    public void setCourseCompletion(String email, Long courseId, boolean completed) throws IllegalArgumentException {

        // 1. Buscamos el usuario por su email
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2. Buscamos el curso por su ID
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // 3. Buscamos la matrícula exacta que une a ese usuario con ese curso
        Enrollment enrollment = findByUserAndCourse(user, course)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

        // 4. Actualizamos el estado (true si es completar, false si es descompletar)
        enrollment.setCompleted(completed);

        // 5. Guardamos los cambios
        save(enrollment);

        log.info("User '{}' marked course ID {} completion as: {}.", email, courseId, completed);
    }

    public void enrollUser(String email, Long courseId) throws IllegalArgumentException, IllegalStateException {

        // 1. Buscamos al usuario (si no existe, lanza IllegalArgumentException)
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2. Buscamos el curso (si no existe, lanza IllegalArgumentException)
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // 3. Comprobamos regla de negocio: ¿Ya está matriculado? (si sí, lanza
        // IllegalStateException)
        if (findByUserAndCourse(user, course).isPresent()) {
            throw new IllegalStateException("already_enrolled");
        }

        // 4. Creamos la matrícula y la guardamos
        Enrollment newEnrollment = new Enrollment(user, course);
        save(newEnrollment);

        log.info("User '{}' successfully enrolled in course ID {}.", email, courseId);
    }

    public void cancelUserEnrollment(String email, Long courseId) throws IllegalArgumentException {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        removeEnrollment(user, course);
        log.info("User '{}' cancelled enrollment for course ID {}.", email, courseId);
    }
}