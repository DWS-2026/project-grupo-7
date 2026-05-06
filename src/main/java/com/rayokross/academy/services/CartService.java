package com.rayokross.academy.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.rayokross.academy.models.Course;

@Service
@SessionScope
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    private final List<Course> cartCourses = new ArrayList<>();

    public List<Course> getCart() {
        return cartCourses;
    }

    public void addCourse(Course course) {
        if (cartCourses.size() >= 50) {
            log.warn("Security Alert: Intento de superar el límite del carrito en sesión WEB.");
            throw new IllegalStateException("No puedes añadir más de 50 cursos al carrito.");
        }

        boolean alreadyExists = false;
        for (Course c : cartCourses) {
            if (c.getId().equals(course.getId())) {
                alreadyExists = true;
                break;
            }
        }

        if (!alreadyExists) {
            cartCourses.add(course);
            log.info("Curso ID {} añadido al carrito de sesión WEB.", course.getId());
        }
    }

    public void removeCourse(Long courseId) {
        boolean removed = cartCourses.removeIf(c -> c.getId().equals(courseId));
        if (removed) {
            log.info("Curso ID {} eliminado del carrito de sesión WEB.", courseId);
        }
    }

    public double getTotalPrice() {
        return cartCourses.stream().mapToDouble(Course::getPrice).sum();
    }

    public void clearCart() {
        cartCourses.clear();
        log.info("Carrito de sesión WEB vaciado.");
    }

    public int getSize() {
        return cartCourses.size();
    }

    public boolean isCourseInCart(Long courseId) {
        return cartCourses.stream().anyMatch(c -> c.getId().equals(courseId));
    }

    public void processApiCheckout(List<Long> courseIds, String userEmail) {

        if (courseIds == null || courseIds.isEmpty()) {
            throw new IllegalArgumentException("El carrito enviado está vacío.");
        }

        if (courseIds.size() > 50) {
            log.warn("Security Alert: Intento de compra masiva por API detectado para el usuario: {}", userEmail);
            throw new IllegalArgumentException("No puedes procesar más de 50 cursos por transacción.");
        }

        List<Course> courses = courseIds.stream()
                .map(id -> courseService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("El curso con ID " + id + " no existe.")))
                .collect(Collectors.toList());

        enrollmentService.checkoutCart(userEmail, courses);

        log.info("API Checkout completado con éxito: Usuario {}, Cursos comprados: {}", userEmail, courseIds.size());
    }
}