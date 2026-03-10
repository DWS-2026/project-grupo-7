package com.rayokross.academy.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.repositories.CourseRepository;

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

    public void save(Course course, MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            try {
                course.setImage(new SerialBlob(imageFile.getBytes()));
            } catch (Exception e) {
                throw new IOException("Failed to create image blob", e);
            }
        }
        this.save(course);
    }
    public List<Course> findByLevel(String level) {
        return repository.findByLevel(level);
    }

}
