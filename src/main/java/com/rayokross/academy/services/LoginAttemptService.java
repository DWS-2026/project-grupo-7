package com.rayokross.academy.services;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private static final Logger log = LoggerFactory.getLogger(LoginAttemptService.class);

    private final int MAX_ATTEMPT = 3;
    private final long BLOCK_DURATION_MS = 15 * 60 * 1000;  

    private static class Attempt {
        final int attempts;
        final long lastModified;

        Attempt(int attempts, long lastModified) {
            this.attempts = attempts;
            this.lastModified = lastModified;
        }
    }

    private ConcurrentHashMap<String, Attempt> attemptsCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
    }

    public void loginFailed(String key) {
        int attempts = 0;
        Attempt cached = attemptsCache.get(key);
        long currentTime = System.currentTimeMillis();
        
        if (cached != null) {
            if (currentTime - cached.lastModified > BLOCK_DURATION_MS) {
                attempts = 1;
            } else {
                attempts = cached.attempts + 1;
            }
        } else {
            attempts = 1;
        }
        
        attemptsCache.put(key, new Attempt(attempts, currentTime));
        log.warn("Failed attempt {}/{} for user {}", attempts, MAX_ATTEMPT, key);
    }

    public boolean isBlocked(String key) {
        Attempt cached = attemptsCache.get(key);
        
        if (cached == null) {
            return false;
        }
        
        if (cached.attempts >= MAX_ATTEMPT && System.currentTimeMillis() - cached.lastModified > BLOCK_DURATION_MS) {
            attemptsCache.remove(key);
            return false;
        }
        
        return cached.attempts >= MAX_ATTEMPT;
    }
}