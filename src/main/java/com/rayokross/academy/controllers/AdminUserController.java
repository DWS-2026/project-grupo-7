package com.rayokross.academy.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.rayokross.academy.models.User;
import com.rayokross.academy.services.UserService;

@Controller
public class AdminUserController {

    private static final Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public String listAllUsers(Model model) {
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute("pageTitle", "User Management");
        return "admin_users_list";
    }

    @GetMapping("/admin/users/{id}")
    public String showUserProfile(@PathVariable Long id, Model model) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isPresent()) {
            model.addAttribute("userProfile", userOpt.get());
            model.addAttribute("pageTitle", "Edit User");
            return "admin_user_profile";
        }
        return "redirect:/admin?error=user_not_found";
    }

    @PostMapping("/admin/users/{id}/edit")
    public String editUser(@PathVariable Long id, @RequestParam String firstName, @RequestParam String lastName) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (firstName != null && !firstName.trim().isEmpty() && lastName != null && !lastName.trim().isEmpty()) {
                user.setFirstName(HtmlUtils.htmlEscape(firstName.trim()));
                user.setLastName(HtmlUtils.htmlEscape(lastName.trim()));
                userService.save(user);
                log.info("Admin updated profile for user ID: {}", id);
            }
            return "redirect:/admin/users/" + id + "?success=true";
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isPresent() && !userOpt.get().getRoles().contains("ADMIN")) {
            userService.deleteById(id);
            log.info("Admin deleted user ID: {}", id);
        }
        return "redirect:/admin/users";
    }
}