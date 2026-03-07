package com.rayokross.academy.services;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repository;

    public List<Course> findAll() {
        return repository.findAll();
    }

    public Optional<Course> findById(long id) {
        return repository.findById(id);
    }

    public void save(Course course) {
        repository.save(course);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

}
