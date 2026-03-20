package com.rayokross.academy.services;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.User;
import com.rayokross.academy.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);
    @Autowired
    private EnrollmentRepository enrollmentRepository;

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
}