package com.rayokross.academy.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rayokross.academy.models.User;
import com.rayokross.academy.repositories.UserRepository;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.debug("Password encoded for user: {}", user.getEmail());
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of("USER"));
            log.debug("'USER' role assigned to: {}", user.getEmail());
        }

        userRepository.save(user);
        log.info("User saved successfully. ID: {}, Email: {}", user.getId(), user.getEmail());
    }

    public void save(User user, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                user.setProfilePhoto(new SerialBlob(imageFile.getBytes()));
                log.debug("Profile photo created successfully for user: {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to create profile photo for user {}: {}", user.getEmail(), e.getMessage(), e);
                throw new IOException("Failed to create profile photo blob", e);
            }
        }

        this.save(user);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
        log.info("User with ID {} deleted by admin.", id);
    }
}