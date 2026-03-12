package com.team1.f1_api.controller;

import com.team1.f1_api.model.AppUser;
import com.team1.f1_api.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * REST controller for authentication-related endpoints.
 * Supports both local username/password registration + login
 * and Google OAuth2 login. The /me endpoint works for both auth types.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user with username, email, and password.
     *
     * @param body map containing "username", "email", and "password"
     * @return the created user info or an error message
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String email = body.get("email");
        String password = body.get("password");

        if (username == null || email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username, email, and password are required"));
        }

        if (userRepo.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username is already taken"));
        }

        if (userRepo.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is already registered"));
        }

        AppUser user = new AppUser(username, email, passwordEncoder.encode(password), "local");
        userRepo.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Registration successful",
                "username", user.getUsername(),
                "email", user.getEmail()
        ));
    }

    /**
     * Logs in a user with username and password.
     * Creates an HTTP session on success.
     *
     * @param body map containing "username" and "password"
     * @param session the HTTP session
     * @return user info on success, or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
        }

        return userRepo.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            user.getUsername(), null, List.of(new SimpleGrantedAuthority(user.getRole()))
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    new HttpSessionSecurityContextRepository()
                            .saveContext(SecurityContextHolder.getContext(), request, response);
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("email", user.getEmail());
                    session.setAttribute("provider", user.getProvider());
                    return ResponseEntity.ok(Map.of(
                            "message", "Login successful",
                            "username", user.getUsername(),
                            "email", user.getEmail()
                    ));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid username or password")));
    }

    /**
     * Returns the current authenticated user's profile info.
     * Works for both local (session-based) and OAuth2 (Google) users.
     *
     * @param oauthUser the OAuth2 principal (null for local users)
     * @param session the HTTP session
     * @return a map containing the user's name, email, and picture
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal OAuth2User oauthUser, HttpSession session) {
        // OAuth2 user (Google login)
        if (oauthUser != null) {
            return ResponseEntity.ok(Map.of(
                    "name", oauthUser.getAttribute("name"),
                    "email", oauthUser.getAttribute("email"),
                    "picture", oauthUser.getAttribute("picture")
            ));
        }

        // Local user (session-based login)
        String username = (String) session.getAttribute("username");
        if (username != null) {
            return ResponseEntity.ok(Map.of(
                    "name", username,
                    "email", session.getAttribute("email"),
                    "picture", ""
            ));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
    }

    /**
     * Logs out the current user by invalidating the session.
     *
     * @param session the HTTP session
     * @return a success message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
