package com.rayokross.academy.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.repositories.CourseRepository;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository repository;

    public List<Course> findAll() {
        return repository.findAll();
    }

    public Page<Course> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Course> findById(long id) {
        return repository.findById(id);
    }

    public void save(Course course) {
        repository.save(course);
        log.info("Course saved successfully. ID: {}", course.getId());
    }

    public void save(Course course, MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            try {
                course.setImage(new SerialBlob(imageFile.getBytes()));
                log.debug("Image created for course ID: {}", course.getId());
            } catch (Exception e) {
                log.error("Failed to create image for course ID {}: {}", course.getId(), e.getMessage(), e);
                throw new IOException("Failed to create image blob", e);
            }
        }
        this.save(course);
    }

    public void delete(long id) {
        repository.deleteById(id);
        log.info("Course with ID {} deleted.", id);
    }

    public List<Course> findByLevel(String level) {
        return repository.findByLevel(level);
    }

    public Page<Course> findByLevel(String level, Pageable pageable) {
        return repository.findByLevel(level, pageable);
    }
}