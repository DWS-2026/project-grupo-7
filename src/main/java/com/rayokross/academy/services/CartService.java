package com.rayokross.academy.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.rayokross.academy.models.Course;

@Service
@SessionScope
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private List<Course> cartCourses = new ArrayList<>();

    public List<Course> getCart() {
        return cartCourses;
    }

    public void addCourse(Course course) {

        boolean alreadyExists = false;

        for (Course c : cartCourses) {
            if (c.getId().equals(course.getId())) {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists) {
            cartCourses.add(course);
            log.info("Course ID {} added to the cart.", course.getId());
        }
    }

    public void removeCourse(Long courseId) {
        boolean removed = false;

        Iterator<Course> iterator = cartCourses.iterator();

        while (iterator.hasNext()) {
            Course course = iterator.next();
            if (course.getId().equals(courseId)) {
                iterator.remove();
                removed = true;
                break;
            }
        }
        if (removed) {
            log.info("Course ID {} removed from cart.", courseId);
        }
    }

    public double getTotalPrice() {
        double total = 0;

        for (Course course : cartCourses) {
            total += course.getPrice();
        }

        return total;
    }

    public void clearCart() {
        cartCourses.clear();
        log.info("Session cart cleared.");
    }

    public int getSize() {
        return cartCourses.size();
    }
}