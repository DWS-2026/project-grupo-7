package com.rayokross.academy.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.User;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUser(User user);

    Optional<Enrollment> findByUserAndCourse(User currentUser, Course course);

    Optional<Enrollment> findByUserEmailAndCourse(String email, Course course);

    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);

}