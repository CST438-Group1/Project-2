package com.team1.f1_api.controller;

// Adds /me endpoint to allow user to view their information
import com.team1.f1_api.model.User;
import com.team1.f1_api.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MeController {
    public final UserService userService;
    public MeController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User oauthUser) {
        User user = userService.findOrCreateUser(oauthUser);
        return Map.of(
                "userId", user.getUserId(),
                "email", user.getEmail(),
                "username", user.getUsername(),
                "role", user.getRole()
        );
    }
}
