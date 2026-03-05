package com.team1.f1_api.repository;

import com.team1.f1_api.model.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link AppUser} entities.
 * Provides CRUD operations and custom finders for user lookup.
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username to search for
     * @return an Optional containing the matching AppUser, or empty if not found
     */
    Optional<AppUser> findByUsername(String username);

    /**
     * Finds a user by email address.
     *
     * @param email the email to search for
     * @return an Optional containing the matching AppUser, or empty if not found
     */
    Optional<AppUser> findByEmail(String email);

    /**
     * Checks if a username is already taken.
     *
     * @param username the username to check
     * @return true if the username exists
     */
    boolean existsByUsername(String username);

    /**
     * Checks if an email is already registered.
     *
     * @param email the email to check
     * @return true if the email exists
     */
    boolean existsByEmail(String email);
}
