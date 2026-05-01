package com.rayokross.academy.services;

import java.io.IOException;
import java.sql.Blob;
import java.util.Arrays;
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

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

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

    public List<Course> findByLevel(String level) {
        return repository.findByLevel(level);
    }

    public Page<Course> findByLevel(String level, Pageable pageable) {
        return repository.findByLevel(level, pageable);
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

    public Course createCourse(Course course, MultipartFile imageFile) throws IllegalArgumentException, IOException {
        validateCourse(course);
        validateImage(imageFile, true);

        if (imageFile != null && !imageFile.isEmpty()) {
            course.setImage(createBlobFromMultipartFile(imageFile));
            log.debug("Image created for course ID: {}", course.getId());
        }

        log.info("Course saved successfully. Title: {}", course.getTitle());
        return repository.save(course);
    }

    public Course updateCourse(Long id, Course updatedCourse, MultipartFile imageFile)
            throws IllegalArgumentException, IOException {
        Course existingCourse = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        validateCourse(updatedCourse);
        validateImage(imageFile, false);

        existingCourse.setTitle(updatedCourse.getTitle());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setPrice(updatedCourse.getPrice());
        existingCourse.setLevel(updatedCourse.getLevel());

        if (imageFile != null && !imageFile.isEmpty()) {
            existingCourse.setImage(createBlobFromMultipartFile(imageFile));
            log.debug("Image updated for course ID: {}", id);
        }

        log.info("Course updated successfully. ID: {}", id);
        return repository.save(existingCourse);
    }

    // --- HELPERS PRIVADOS ---

    private void validateCourse(Course course) {
        if (course.getPrice() < 0) {
            throw new IllegalArgumentException("Error: Price of the course can't be negative.");
        }
    }

    private void validateImage(MultipartFile imageFile, boolean isRequired) {
        if (imageFile == null || imageFile.isEmpty()) {
            if (isRequired)
                throw new IllegalArgumentException("Error: You have to upload a photo for the course.");
            return;
        }
        if (!ALLOWED_IMAGE_TYPES.contains(imageFile.getContentType())) {
            throw new IllegalArgumentException("Security error: JPEG, PNG or GIF required.");
        }
        if (imageFile.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Error: Image size can't exceed 5MB.");
        }
    }

    private Blob createBlobFromMultipartFile(MultipartFile file) throws IOException {
        try {
            return new SerialBlob(file.getBytes());
        } catch (Exception e) {
            throw new IOException("Failed to create image blob", e);
        }
    }
}