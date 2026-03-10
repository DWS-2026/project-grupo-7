package com.rayokross.academy.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.rayokross.academy.models.Course;

@Service
@SessionScope
public class CartService {

    private List<Course> cartCourses = new ArrayList<>();

    public List<Course> getCart() {
        return cartCourses;
    }

    public void addCourse(Course course) {

        boolean alreadyExists = cartCourses.stream()
                .anyMatch(c -> c.getId().equals(course.getId()));
                
        if (!alreadyExists) {
            cartCourses.add(course);
        }
    }

    public void removeCourse(Long courseId) {
        cartCourses.removeIf(course -> course.getId().equals(courseId));
    }

    public double getTotalPrice() {
        return cartCourses.stream()
                .mapToDouble(Course::getPrice) 
                .sum();
    }

    public void clearCart() {
        cartCourses.clear();
    }
    
    public int getSize() {
        return cartCourses.size();
    }
}