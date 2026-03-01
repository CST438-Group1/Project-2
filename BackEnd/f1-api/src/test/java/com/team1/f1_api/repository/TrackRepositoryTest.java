package com.team1.f1_api.repository;

import com.team1.f1_api.model.Track;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link TrackRepository}.
 * Tests run against the H2 database seeded by CsvDataLoader on startup.
 * Uses @Transactional so any test inserts are rolled back after each test.
 */
@SpringBootTest
@Transactional
class TrackRepositoryTest {

    @Autowired
    private TrackRepository trackRepository;

    @Test
    void testFindAllReturnsSeededTracks() {
        List<Track> tracks = trackRepository.findAll();
        assertFalse(tracks.isEmpty(), "Tracks should be seeded from CSV");
        assertTrue(tracks.size() > 50, "CSV contains many unique tracks");
    }

    @Test
    void testFindByNameAndLayoutFindsSeededTrack() {
        Optional<Track> found = trackRepository.findByNameAndLayout("Albert Park", "Full Circuit");
        assertTrue(found.isPresent());
        assertEquals("Australia", found.get().getCountry());
        assertEquals("AU", found.get().getRegion());
        assertEquals(5.278, found.get().getLengthKm());
    }

    @Test
    void testFindByNameAndLayoutNotFound() {
        Optional<Track> found = trackRepository.findByNameAndLayout("Nonexistent Track", "No Layout");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByIdForSeededTrack() {
        Track albertPark = trackRepository.findByNameAndLayout("Albert Park", "Full Circuit").orElseThrow();
        Optional<Track> found = trackRepository.findById(albertPark.getTrackId());
        assertTrue(found.isPresent());
        assertEquals("Albert Park", found.get().getName());
    }

    @Test
    void testSaveNewTrack() {
        long countBefore = trackRepository.count();
        Track newTrack = new Track("TEST", "Testland", "Unit Test Circuit", "Test Layout", 2.5);
        Track saved = trackRepository.save(newTrack);

        assertNotNull(saved.getTrackId());
        assertEquals(countBefore + 1, trackRepository.count());
        assertEquals("Unit Test Circuit", saved.getName());
    }

    @Test
    void testDeleteNewTrack() {
        Track newTrack = trackRepository.save(
                new Track("TEST", "Testland", "Delete Me Circuit", "Test Layout", 1.0));
        long countAfterInsert = trackRepository.count();

        trackRepository.deleteById(newTrack.getTrackId());
        assertEquals(countAfterInsert - 1, trackRepository.count());
    }
}
