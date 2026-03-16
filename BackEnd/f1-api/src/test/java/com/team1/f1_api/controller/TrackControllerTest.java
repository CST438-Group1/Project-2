package com.team1.f1_api.controller;

import com.team1.f1_api.model.Track;
import com.team1.f1_api.repository.TrackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TrackController}.
 * Uses Mockito to mock the TrackRepository dependency.
 */
class TrackControllerTest {
    // ── getAllTracks ──────────────────────────────────────────────────────────

    @Test
    void testGetAllTracksReturnsListOfTracks() {
        TrackRepository mockRepo = mock(TrackRepository.class);
        List<Track> expectedTracks = Arrays.asList(
                new Track("AU", "Australia", "Albert Park", "Full Circuit", 5.278),
                new Track("EU", "Belgium", "Zolder", "Full Circuit", 3.98)
        );
        when(mockRepo.findAll()).thenReturn(expectedTracks);

        TrackController controller = new TrackController(mockRepo);
        List<Track> result = controller.getAllTracks();

        assertEquals(2, result.size());
        assertEquals("Albert Park", result.get(0).getName());
        assertEquals("Zolder", result.get(1).getName());
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void testGetAllTracksReturnsEmptyList() {
        TrackRepository mockRepo = mock(TrackRepository.class);
        when(mockRepo.findAll()).thenReturn(Collections.emptyList());

        TrackController controller = new TrackController(mockRepo);
        List<Track> result = controller.getAllTracks();

        assertTrue(result.isEmpty());
        verify(mockRepo, times(1)).findAll();
    }
    // ── getTrackById ──────────────────────────────────────────────────────────

    @Test
    void testGetTrackByIdReturns200WhenFound() {
        TrackRepository mockRepo = mock(TrackRepository.class);
        Track track = new Track("AU", "Australia", "Albert Park", "Full Circuit", 5.278);
        when(mockRepo.findById(1L)).thenReturn(Optional.of(track));

        TrackController controller = new TrackController(mockRepo);
        ResponseEntity<Track> response = controller.getTrackById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Albert Park", response.getBody().getName());
    }

    @Test
    void testGetTrackByIdReturns404WhenNotFound() {
        TrackRepository mockRepo = mock(TrackRepository.class);
        when(mockRepo.findById(99L)).thenReturn(Optional.empty());

        TrackController controller = new TrackController(mockRepo);
        ResponseEntity<Track> response = controller.getTrackById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ── createTrack ───────────────────────────────────────────────────────────

    @Test
    void testCreateTrackReturns201AndSavedTrack() {
        TrackRepository mockRepo = mock(TrackRepository.class);
        Track input = new Track("EU", "Italy", "Monza", "Full Circuit", 5.793);
        when(mockRepo.save(input)).thenReturn(input);

        TrackController controller = new TrackController(mockRepo);
        ResponseEntity<Track> response = controller.createTrack(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Monza", response.getBody().getName());
        verify(mockRepo, times(1)).save(input);
    }

    // ── updateTrack ───────────────────────────────────────────────────────────

    @Test
    void testUpdateTrackReturns200AndUpdatesExistingEntity() {
        TrackRepository mockRepo = mock(TrackRepository.class);
        Track existing = new Track("EU", "Italy", "Monza", "Full Circuit", 5.793);
        Track updated = new Track("EU", "Italy", "Monza", "Junior Circuit", 2.405);
        when(mockRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(mockRepo.save(existing)).thenReturn(existing);

        TrackController controller = new TrackController(mockRepo);
        ResponseEntity<Track> response = controller.updateTrack(1L, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(existing, response.getBody());
        assertEquals("Junior Circuit", response.getBody().getLayout());
        assertEquals(2.405, response.getBody().getLengthKm());
    }

    @Test
    void testUpdateTrackReturns404WhenNotFound() {
        TrackRepository mockRepo = mock(TrackRepository.class);
        when(mockRepo.findById(99L)).thenReturn(Optional.empty());

        TrackController controller = new TrackController(mockRepo);
        ResponseEntity<Track> response = controller.updateTrack(99L,
                new Track("EU", "Italy", "Monza", "Junior Circuit", 2.405));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ── deleteTrack ───────────────────────────────────────────────────────────

    @Test
    void testDeleteTrackReturns204WhenFound() {
        TrackRepository mockRepo = mock(TrackRepository.class);
        when(mockRepo.existsById(1L)).thenReturn(true);

        TrackController controller = new TrackController(mockRepo);
        ResponseEntity<Void> response = controller.deleteTrack(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(mockRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTrackReturns404WhenNotFound() {
        TrackRepository mockRepo = mock(TrackRepository.class);
        when(mockRepo.existsById(99L)).thenReturn(false);

        TrackController controller = new TrackController(mockRepo);
        ResponseEntity<Void> response = controller.deleteTrack(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(mockRepo, never()).deleteById(any());
    }
}
