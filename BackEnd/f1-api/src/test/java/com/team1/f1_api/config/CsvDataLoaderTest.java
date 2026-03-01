package com.team1.f1_api.config;

import com.team1.f1_api.model.FastestLap;
import com.team1.f1_api.repository.FastestLapRepository;
import com.team1.f1_api.repository.TrackRepository;
import com.team1.f1_api.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for {@link CsvDataLoader}.
 * Verifies that the CSV import runs successfully on startup and
 * populates the tracks, vehicles, and fastest_laps tables.
 * Uses an H2 in-memory database configured in test application.properties.
 */
@SpringBootTest
class CsvDataLoaderTest {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private FastestLapRepository fastestLapRepository;

    @Test
    void testCsvImportPopulatedTracks() {
        long trackCount = trackRepository.count();
        assertTrue(trackCount > 0, "Tracks table should have been seeded from CSV");
    }

    @Test
    void testCsvImportPopulatedVehicles() {
        long vehicleCount = vehicleRepository.count();
        assertTrue(vehicleCount > 0, "Vehicles table should have been seeded from CSV");
    }

    @Test
    void testCsvImportPopulatedFastestLaps() {
        long lapCount = fastestLapRepository.count();
        assertTrue(lapCount > 0, "Fastest laps table should have been seeded from CSV");
    }

    @Test
    void testCsvImportLapCount() {
        // The CSV has 13,262 data rows (header + title row excluded)
        long lapCount = fastestLapRepository.count();
        assertEquals(13262, lapCount, "All 13,262 lap records should be imported");
    }

    @Test
    void testKnownTrackExists() {
        // Albert Park should exist from the first rows of the CSV
        assertTrue(trackRepository.findByNameAndLayout("Albert Park", "Full Circuit").isPresent(),
                "Albert Park Full Circuit should exist in the database");
    }

    @Test
    void testKnownVehicleExists() {
        // McLaren MCL39 appears in the first data rows
        assertTrue(vehicleRepository.findByName("McLaren MCL39").isPresent(),
                "McLaren MCL39 should exist in the database");
    }

    @Test
    void testFastestLapHasValidReferences() {
        // Verify a lap record has proper foreign key references
        FastestLap anyLap = fastestLapRepository.findAll().stream().findFirst().orElse(null);
        assertNotNull(anyLap, "At least one lap record should exist");
        assertNotNull(anyLap.getTrack(), "Lap should reference a track");
        assertNotNull(anyLap.getVehicle(), "Lap should reference a vehicle");
        assertNotNull(anyLap.getLapTime(), "Lap should have a lap time");
    }
}
