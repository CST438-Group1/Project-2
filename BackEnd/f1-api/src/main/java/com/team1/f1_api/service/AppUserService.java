package com.team1.f1_api.service;

import com.team1.f1_api.model.AppUser;
import com.team1.f1_api.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * Service handling user authentication for both local and Google OAuth2 logins.
 * Implements {@link UserDetailsService} for Spring Security session-based auth
 * and {@link DefaultOAuth2UserService} for Google OAuth2 login.
 */
@Service
public class AppUserService extends DefaultOAuth2UserService implements UserDetailsService {

    private final AppUserRepository userRepo;

    /**
     * Constructs the service with the required AppUserRepository dependency.
     *
     * @param userRepo the JPA repository for AppUser entities
     */
    public AppUserService(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Loads a user by username for Spring Security authentication.
     * Used during local username/password login.
     *
     * @param username the username to look up
     * @return a UserDetails object containing the user's credentials and authorities
     * @throws UsernameNotFoundException if no user exists with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
    }
    /**
     * Loads or creates a user during Google OAuth2 login.
     * If the user does not exist in the database, a new AppUser is created
     * with provider set to "google". Returns a DefaultOAuth2User with the
     * role loaded from the database.
     *
     * @param userRequest the OAuth2 user request containing Google credentials
     * @return an OAuth2User with authorities mapped from the database role
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauthUser = super.loadUser(userRequest);

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        AppUser appUser = userRepo.findByEmail(email).orElseGet(() -> {
            AppUser newUser = new AppUser(name, email, null, "google");
            newUser.setPicture(picture);
            return userRepo.save(newUser);
        });
        return new org.springframework.security.oauth2.core.user.DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(appUser.getRole())),
                oauthUser.getAttributes(),
                "email"
        );
    }
}