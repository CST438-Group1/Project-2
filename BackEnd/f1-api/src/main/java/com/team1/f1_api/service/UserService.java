package com.team1.f1_api.service;

// Adds user service which will handle the repo and login. Allows other controllers
// to search for user. Creates newUser and inserts to database successfully.
import com.team1.f1_api.model.User;
import com.team1.f1_api.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User findOrCreateUser (OAuth2User oAuth2User){
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(name,email);
            return userRepository.save(newUser);
        });
    }
    public User getCurrentUser (OAuth2User oAuth2User){
        return findOrCreateUser(oAuth2User);
    }
}
