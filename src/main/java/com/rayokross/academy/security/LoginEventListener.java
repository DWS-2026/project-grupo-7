package com.rayokross.academy.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.rayokross.academy.services.LoginAttemptService;

@Component
public class LoginEventListener {

    private static final Logger log = LoggerFactory.getLogger(LoginEventListener.class);

    @Autowired
    private LoginAttemptService loginAttemptService;

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        loginAttemptService.loginFailed(username);
    }

    @EventListener
    public void authenticationLocked(AuthenticationFailureLockedEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        log.warn("Blocked login attempt prevented for locked user {}", username);
    }

    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
            log.info("Login successful for user {}", username);
            loginAttemptService.loginSucceeded(username);
        }
    }
}