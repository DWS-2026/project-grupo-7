package com.rayokross.academy.controllers;

import java.security.Principal;
import java.sql.Blob;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.rayokross.academy.models.User;
import com.rayokross.academy.services.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        Optional<User> userOptional = userService.findByEmail(principal.getName());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);

            model.addAttribute("isAdmin", user.getRoles().contains("ADMIN"));
            model.addAttribute("enrollments", user.getEnrollments()); 

            model.addAttribute("pageTitle", "My Profile");
            return "profile";
        }
        return "redirect:/login";
    }

    @PostMapping("/profile/upload-photo")
    public String uploadPhoto(@RequestParam MultipartFile photo, Principal principal) throws Exception {
        if (principal == null)
            return "redirect:/login";

        Optional<User> userOpt = userService.findByEmail(principal.getName());
        if (userOpt.isPresent() && !photo.isEmpty()) {
            User user = userOpt.get();

            byte[] bytes = photo.getBytes();
            Blob blob = new SerialBlob(bytes);

            user.setProfilePhoto(blob);
            userService.save(user);
        }
        return "redirect:/profile";
    }

    @GetMapping("/user/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws Exception {
        Optional<User> user = userService.findById(id); 

        if (user.isPresent() && user.get().getProfilePhoto() != null) {
            Blob image = user.get().getProfilePhoto();
            Resource file = new InputStreamResource(image.getBinaryStream());

            MediaType mediaType = MediaTypeFactory.getMediaType(file).orElse(MediaType.IMAGE_JPEG);

            return ResponseEntity.ok().contentType(mediaType).body(file);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam String fullName, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        Optional<User> userOpt = userService.findByEmail(principal.getName());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String[] names = fullName.split(" ", 2);
            user.setFirstName(names[0]);
            if (names.length > 1)
                user.setLastName(names[1]);

            userService.save(user);
        }
        return "redirect:/profile?profileSuccess=true";
    }
}
