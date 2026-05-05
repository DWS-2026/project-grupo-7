package com.rayokross.academy.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
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

    private final Path root = Paths.get("uploads");

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
                course.setImage(BlobProxy.generateProxy(imageFile.getBytes()));
                log.debug("Image created for course ID: {}", course.getId());
            } catch (IOException e) {
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

    public Course createCourse(Course course, MultipartFile imageFile, MultipartFile syllabusFile) throws IOException {
        validateCourse(course);
        validateImage(imageFile, false);

        course.setDescription(sanitize(course.getDescription()));

        if (imageFile != null && !imageFile.isEmpty()) {
            course.setImage(createBlobFromMultipartFile(imageFile));
        }

        if (syllabusFile != null && !syllabusFile.isEmpty()) {
            saveSyllabus(course, syllabusFile);
        }

        log.info("Course created successfully with syllabus: {}", course.getSyllabusFileName());
        return repository.save(course);
    }

    public Course updateCourse(Long id, Course updatedCourse, MultipartFile imageFile, MultipartFile syllabusFile)
            throws IOException {
        Course existingCourse = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        validateCourse(updatedCourse);
        validateImage(imageFile, false);

        existingCourse.setTitle(updatedCourse.getTitle());
        existingCourse.setDescription(sanitize(updatedCourse.getDescription()));
        existingCourse.setPrice(updatedCourse.getPrice());
        existingCourse.setLevel(updatedCourse.getLevel());

        if (imageFile != null && !imageFile.isEmpty()) {
            existingCourse.setImage(createBlobFromMultipartFile(imageFile));
        }

        if (syllabusFile != null && !syllabusFile.isEmpty()) {
            saveSyllabus(existingCourse, syllabusFile);
        }

        return repository.save(existingCourse);
    }

    public void updateCourseImage(Long id, MultipartFile imageFile) throws IOException {
        Course course = repository.findById(id).orElseThrow();
        Blob blob = BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize());
        course.setImage(blob);
        repository.save(course);
    }

    private String sanitize(String html) {
        if (html == null)
            return null;
        return Jsoup.clean(html, Safelist.basic());
    }

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
            return BlobProxy.generateProxy(file.getBytes());
        } catch (IOException e) {
            throw new IOException("Failed to create image blob", e);
        }
    }

    public void saveSyllabus(Course course, MultipartFile file) throws IOException {
        if (file.isEmpty())
            return;

        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        String fileName = file.getOriginalFilename();
        course.setSyllabusFileName(fileName);

        Files.copy(file.getInputStream(), this.root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
    }
}