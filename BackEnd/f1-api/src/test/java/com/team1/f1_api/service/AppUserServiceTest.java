package com.team1.f1_api.service;

import com.team1.f1_api.model.AppUser;
import com.team1.f1_api.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

    // ── loadUserByUsername ────────────────────────────────────────────────────

    @Test
    void testLoadUserByUsernameReturnsUserDetails() {
        AppUserRepository mockRepo = mock(AppUserRepository.class);
        AppUser user = new AppUser("james", "james@email.com", "hashedpw", "local");
        when(mockRepo.findByUsername("james")).thenReturn(Optional.of(user));

        AppUserService service = new AppUserService(mockRepo);
        UserDetails result = service.loadUserByUsername("james");

        assertEquals("james", result.getUsername());
        assertEquals("hashedpw", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER")));
    }

    @Test
    void testLoadUserByUsernameThrowsWhenNotFound() {
        AppUserRepository mockRepo = mock(AppUserRepository.class);
        when(mockRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        AppUserService service = new AppUserService(mockRepo);

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown"));
    }

    @Test
    void testLoadUserByUsernameReturnsAdminAuthority() {
        AppUserRepository mockRepo = mock(AppUserRepository.class);
        AppUser admin = new AppUser("adminuser", "admin@email.com", "hashedpw", "local");
        admin.setRole("ADMIN");
        when(mockRepo.findByUsername("adminuser")).thenReturn(Optional.of(admin));

        AppUserService service = new AppUserService(mockRepo);
        UserDetails result = service.loadUserByUsername("adminuser");

        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN")));
    }
}