package com.rayokross.academy.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.rayokross.academy.models.User;
import com.rayokross.academy.repositories.UserRepository;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/jpeg", "image/png", "image/gif", "image/avif", "image/webp");

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

    public void adminUpdateUserProfile(Long id, String firstName, String lastName) throws IllegalArgumentException {
        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Names cannot be empty");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Usamos el import de HtmlUtils
        user.setFirstName(HtmlUtils.htmlEscape(firstName.trim()));
        user.setLastName(HtmlUtils.htmlEscape(lastName.trim()));

        userRepository.save(user);
        log.info("Admin updated profile for user ID: {}", id);
    }

    public void deleteUserSafe(Long id) throws IllegalStateException {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRoles().contains("ADMIN")) {
            throw new IllegalStateException("cannot_delete_admin");
        }

        userRepository.deleteById(id);
        log.info("Admin deleted user ID: {}", id);
    }

    public void registerNewUser(String firstName, String lastName, String email, String password)
            throws IllegalArgumentException {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("The name is obligatory.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("The last name is obligatory.");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("The password must contain 8 characters.");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email == null || !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Please, introduce a valid email.");
        }
        if (existEmail(email)) {
            throw new IllegalArgumentException("This email is already registered.");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setRoles(List.of("USER"));
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
        log.info("New user registered successfully with email: '{}'", email);
    }

    public void updateUserProfile(String email, String fullName) throws IllegalArgumentException {
        if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100) {
            throw new IllegalArgumentException("invalid_name");
        }
        if (fullName.contains("<") || fullName.contains(">")) {
            throw new IllegalArgumentException("invalid_name");
        }

        String[] names = fullName.split(" ", 2);
        if (names.length < 2 || names[1].trim().isEmpty()) {
            throw new IllegalArgumentException("last_name_required");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setFirstName(names[0]);
        user.setLastName(names[1].trim());

        userRepository.save(user);
        log.info("User '{}' updated their profile name.", user.getEmail());
    }

    public void updateProfilePhoto(String email, MultipartFile photo)
            throws IllegalArgumentException, IOException {
        if (photo == null || photo.isEmpty())
            return;

        if (!ALLOWED_IMAGE_TYPES.contains(photo.getContentType())) {
            throw new IllegalArgumentException("invalid_format");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        try {
            user.setProfilePhoto(new SerialBlob(photo.getBytes()));
            userRepository.save(user);
            log.info("User '{}' successfully updated their profile photo.", email);
        } catch (Exception e) {
            throw new IOException("Failed to process profile photo", e);
        }
    }
}