package com.rayokross.academy.repositories;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUser(User user);
    Optional<Enrollment> findByUserAndCourse(User currentUser, Course course);
}