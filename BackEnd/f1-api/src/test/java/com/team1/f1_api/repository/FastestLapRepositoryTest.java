package com.team1.f1_api.repository;

import com.team1.f1_api.model.FastestLap;
import com.team1.f1_api.model.Track;
import com.team1.f1_api.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link FastestLapRepository}.
 * Verifies custom query methods and entity relationships.
 * Tests run against the H2 database seeded by CsvDataLoader on startup.
 * Uses @Transactional so any test inserts are rolled back after each test.
 */
@SpringBootTest
@Transactional
class FastestLapRepositoryTest {

    @Autowired
    private FastestLapRepository fastestLapRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void testFindAllReturnsSeededLaps() {
        List<FastestLap> laps = fastestLapRepository.findAll();
        assertFalse(laps.isEmpty(), "Fastest laps should be seeded from CSV");
    }

    @Test
    void testCountMatchesCsvRows() {
        assertEquals(13262, fastestLapRepository.count(), "All 13,262 CSV rows should be imported");
    }

    @Test
    void testFindByTrackTrackId() {
        Track albertPark = trackRepository.findByNameAndLayout("Albert Park", "Full Circuit").orElseThrow();
        List<FastestLap> laps = fastestLapRepository.findByTrackTrackId(albertPark.getTrackId());
        assertFalse(laps.isEmpty(), "Albert Park should have lap records");
        // All returned laps should reference Albert Park
        laps.forEach(lap -> assertEquals("Albert Park", lap.getTrack().getName()));
    }

    @Test
    void testFindByVehicleVehicleId() {
        Vehicle mclaren = vehicleRepository.findByName("McLaren MCL39").orElseThrow();
        List<FastestLap> laps = fastestLapRepository.findByVehicleVehicleId(mclaren.getVehicleId());
        assertFalse(laps.isEmpty(), "McLaren MCL39 should have lap records");
        // All returned laps should reference McLaren MCL39
        laps.forEach(lap -> assertEquals("McLaren MCL39", lap.getVehicle().getName()));
    }

    @Test
    void testFindByDriver() {
        List<FastestLap> norrisLaps = fastestLapRepository.findByDriver("Lando Norris");
        assertFalse(norrisLaps.isEmpty(), "Lando Norris should have lap records in the CSV");
        norrisLaps.forEach(lap -> assertEquals("Lando Norris", lap.getDriver()));
    }

    @Test
    void testFindByDriverNotFound() {
        List<FastestLap> laps = fastestLapRepository.findByDriver("Unknown Driver XYZ");
        assertTrue(laps.isEmpty());
    }

    @Test
    void testSaveNewLap() {
        Track newTrack = trackRepository.save(
                new Track("TEST", "Testland", "Test Circuit", "Test Layout", 2.0));
        Vehicle newVehicle = vehicleRepository.save(new Vehicle("Test Car 9000"));

        long countBefore = fastestLapRepository.count();
        FastestLap saved = fastestLapRepository.save(
                new FastestLap(newTrack, newVehicle, "0:59.99", "Test Driver"));

        assertNotNull(saved.getLapId());
        assertEquals(countBefore + 1, fastestLapRepository.count());
        assertEquals("Test Circuit", saved.getTrack().getName());
        assertEquals("Test Car 9000", saved.getVehicle().getName());
    }

    @Test
    void testLapHasValidForeignKeyReferences() {
        FastestLap anyLap = fastestLapRepository.findAll().stream().findFirst().orElseThrow();
        assertNotNull(anyLap.getTrack(), "Lap should reference a track");
        assertNotNull(anyLap.getVehicle(), "Lap should reference a vehicle");
        assertNotNull(anyLap.getTrack().getName(), "Track should have a name");
        assertNotNull(anyLap.getVehicle().getName(), "Vehicle should have a name");
        assertNotNull(anyLap.getLapTime(), "Lap should have a time");
    }
}
