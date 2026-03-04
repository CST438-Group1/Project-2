package com.team1.f1_api.service;

//Tests creating new user and returning existing user

import com.team1.f1_api.model.User;
import com.team1.f1_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void createsUserIfEmailNotFound() {

        UserRepository repo = mock(UserRepository.class);
        UserService service = new UserService(repo);
        OAuth2User oauth = mock(OAuth2User.class);
        when(oauth.getAttribute("email")).thenReturn("test@example.com");
        when(oauth.getAttribute("name")).thenReturn("Test User");

        when(repo.findByEmail("test@example.com")).thenReturn(Optional.empty());

        when(repo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.findOrCreateUser(oauth);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test User", result.getUsername());

        verify(repo).save(any(User.class));
    }

    @Test
    void returnsExistingUserIfEmailExists() {

        UserRepository repo = mock(UserRepository.class);
        UserService service = new UserService(repo);

        OAuth2User oauth = mock(OAuth2User.class);
        when(oauth.getAttribute("email")).thenReturn("test@example.com");
        when(oauth.getAttribute("name")).thenReturn("Test User");

        User existingUser = new User("Test User", "test@example.com");

        when(repo.findByEmail("test@example.com"))
                .thenReturn(Optional.of(existingUser));

        User result = service.findOrCreateUser(oauth);

        assertEquals(existingUser, result);

        verify(repo, never()).save(any(User.class));
    }
}