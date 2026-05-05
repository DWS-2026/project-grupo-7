package com.rayokross.academy.controllers.web;

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
        try {
            userService.adminUpdateUserProfile(id, firstName, lastName);
            return "redirect:/admin/users/" + id + "?success=true";
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/users/" + id + "?error=invalid_data";
        } catch (Exception e) {
            return "redirect:/admin";
        }
    }

    @PostMapping("/admin/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserSafe(id);
            return "redirect:/admin/users";
        } catch (IllegalStateException e) {
            log.warn("Admin attempted to delete another admin or protected user: ID {}", id);
            return "redirect:/admin/users?error=cannot_delete_admin";
        }
    }
}