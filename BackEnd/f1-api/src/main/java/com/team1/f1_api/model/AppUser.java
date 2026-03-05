package com.team1.f1_api.model;

import jakarta.persistence.*;

/**
 * Entity representing a registered user in the MotoRYX application.
 * Supports both local username/password registration and Google OAuth2 login.
 */
@Entity
@Table(name = "app_users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"}),
    @UniqueConstraint(columnNames = {"email"})
})
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    /** BCrypt-hashed password. Null for OAuth2-only users. */
    private String password;

    /** "local" or "google" — indicates how the user registered. */
    @Column(nullable = false)
    private String provider;

    /** Google profile picture URL (null for local users). */
    private String picture;

    public AppUser() {}

    public AppUser(String username, String email, String password, String provider) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = provider;
    }

    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getProvider() { return provider; }
    public String getPicture() { return picture; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setProvider(String provider) { this.provider = provider; }
    public void setPicture(String picture) { this.picture = picture; }
}
