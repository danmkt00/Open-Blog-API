package com.example.openblogapi.open_blog_api.util;

import com.example.openblogapi.open_blog_api.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class SecurityUtils {
    private SecurityUtils() {}

    public static User getCurrentUserOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            return null; // anonymous
        }

        return (User) auth.getPrincipal();
    }

    public static User getCurrentUser() {
        User user = getCurrentUserOrNull();
        if (user == null) {
            throw new RuntimeException("Unauthorized user");
        }
        return user;
    }
}
