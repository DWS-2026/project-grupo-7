package com.rayokross.academy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.rayokross.academy.services.LoginAttemptService;

@Component
public class LoginEventListener {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        loginAttemptService.loginFailed(username);
    }

    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
            loginAttemptService.loginSucceeded(username);
        }
    }
}