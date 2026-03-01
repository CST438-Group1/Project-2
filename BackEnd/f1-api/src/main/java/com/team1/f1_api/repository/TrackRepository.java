package com.team1.f1_api.repository;

import com.team1.f1_api.model.Track;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Track} entities.
 * Provides standard CRUD operations and custom query methods
 * for accessing track data stored in Supabase PostgreSQL.
 */
public interface TrackRepository extends JpaRepository<Track, Long> {

    /**
     * Finds a track by its name and layout combination.
     * Used during CSV import to avoid duplicate track entries.
     *
     * @param name   the track name (e.g. "Albert Park")
     * @param layout the track layout (e.g. "Full Circuit")
     * @return an Optional containing the matching Track, or empty if not found
     */
    Optional<Track> findByNameAndLayout(String name, String layout);
}
