package com.rayokross.academy.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.repositories.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repository;

    // 1. Mantenemos el original para la página de inicio (index)
    public List<Course> findAll() {
        return repository.findAll();
    }

    // 2. NUEVO: Método para el catálogo completo con paginación
    public Page<Course> findAll(Pageable pageable) {
        return repository.findAll(pageable);
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

    // 3. Mantenemos el original por si lo usáis en otra parte
    public List<Course> findByLevel(String level) {
        return repository.findByLevel(level);
    }

    // 4. NUEVO: Método para filtrar por nivel con paginación
    public Page<Course> findByLevel(String level, Pageable pageable) {
        return repository.findByLevel(level, pageable);
    }
}
