package com.team1.f1_api.controller;

import com.team1.f1_api.model.Track;
import com.team1.f1_api.repository.TrackRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TrackController}.
 * Uses Mockito to mock the TrackRepository dependency.
 */
class TrackControllerTest {

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
}
