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

@Service
public class AppUserService extends DefaultOAuth2UserService implements UserDetailsService {

    private final AppUserRepository userRepo;

    public AppUserService(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }


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