package com.team1.f1_api.controller;

import com.team1.f1_api.model.FastestLap;
import com.team1.f1_api.model.Track;
import com.team1.f1_api.model.Vehicle;
import com.team1.f1_api.repository.FastestLapRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FastestLapControllerTest {
    private FastestLap fakeLap() {
        Track track = new Track("AU", "Australia", "Albert Park", "Full Circuit", 5.278);
        Vehicle vehicle = new Vehicle("FakeCar1");
        return new FastestLap(track, vehicle, "1:15.10", "Fake Driver");
    }

    // ── getAllLaps ────────────────────────────────────────────────────────────

    @Test
    void testGetAllLapsReturnsList() {
        FastestLapRepository mockRepo = mock(FastestLapRepository.class);
        List<FastestLap> laps = Arrays.asList(fakeLap(), fakeLap());
        when(mockRepo.findAll()).thenReturn(laps);

        FastestLapController controller = new FastestLapController(mockRepo);
        List<FastestLap> result = controller.getAllLaps();

        assertEquals(2, result.size());
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void testGetAllLapsReturnsEmptyList() {
        FastestLapRepository mockRepo = mock(FastestLapRepository.class);
        when(mockRepo.findAll()).thenReturn(Collections.emptyList());

        FastestLapController controller = new FastestLapController(mockRepo);
        List<FastestLap> result = controller.getAllLaps();

        assertTrue(result.isEmpty());
        verify(mockRepo, times(1)).findAll();
    }

    // ── getLapById ────────────────────────────────────────────────────────────

    @Test
    void testGetLapByIdReturns200WhenFound() {
        FastestLapRepository mockRepo = mock(FastestLapRepository.class);
        FastestLap lap = fakeLap();
        when(mockRepo.findById(1L)).thenReturn(Optional.of(lap));

        FastestLapController controller = new FastestLapController(mockRepo);
        ResponseEntity<FastestLap> response = controller.getLapById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fake Driver", response.getBody().getDriver());
    }

    @Test
    void testGetLapByIdReturns404WhenNotFound() {
        FastestLapRepository mockRepo = mock(FastestLapRepository.class);
        when(mockRepo.findById(99L)).thenReturn(Optional.empty());

        FastestLapController controller = new FastestLapController(mockRepo);
        ResponseEntity<FastestLap> response = controller.getLapById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ── getLapsByTrack ────────────────────────────────────────────────────────

    @Test
    void testGetLapsByTrackReturnsList() {
        FastestLapRepository mockRepo = mock(FastestLapRepository.class);
        when(mockRepo.findByTrackTrackId(1L)).thenReturn(Arrays.asList(fakeLap(), fakeLap()));

        FastestLapController controller = new FastestLapController(mockRepo);
        List<FastestLap> result = controller.getLapsByTrack(1L);

        assertEquals(2, result.size());
        verify(mockRepo, times(1)).findByTrackTrackId(1L);
    }

    // ── getLapsByVehicle ──────────────────────────────────────────────────────

    @Test
    void testGetLapsByVehicleReturnsList() {
        FastestLapRepository mockRepo = mock(FastestLapRepository.class);
        when(mockRepo.findByVehicleVehicleId(1L)).thenReturn(Arrays.asList(fakeLap()));

        FastestLapController controller = new FastestLapController(mockRepo);
        List<FastestLap> result = controller.getLapsByVehicle(1L);

        assertEquals(1, result.size());
        verify(mockRepo, times(1)).findByVehicleVehicleId(1L);
    }

    // ── getLapsByDriver ───────────────────────────────────────────────────────

    @Test
    void testGetLapsByDriverReturnsList() {
        FastestLapRepository mockRepo = mock(FastestLapRepository.class);
        when(mockRepo.findByDriver("Fake Driver")).thenReturn(Arrays.asList(fakeLap()));

        FastestLapController controller = new FastestLapController(mockRepo);
        List<FastestLap> result = controller.getLapsByDriver("Fake Driver");

        assertEquals(1, result.size());
        assertEquals("Fake Driver", result.get(0).getDriver());
        verify(mockRepo, times(1)).findByDriver("Fake Driver");
    }

    @Test
    void testGetLapsByDriverReturnsEmptyList() {
        FastestLapRepository mockRepo = mock(FastestLapRepository.class);
        when(mockRepo.findByDriver("Unknown Driver")).thenReturn(Collections.emptyList());

        FastestLapController controller = new FastestLapController(mockRepo);
        List<FastestLap> result = controller.getLapsByDriver("Unknown Driver");

        assertTrue(result.isEmpty());
    }
}
