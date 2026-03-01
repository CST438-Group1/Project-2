package com.team1.f1_api.repository;

import com.team1.f1_api.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link VehicleRepository}.
 * Tests run against the H2 database seeded by CsvDataLoader on startup.
 * Uses @Transactional so any test inserts are rolled back after each test.
 */
@SpringBootTest
@Transactional
class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void testFindAllReturnsSeededVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertFalse(vehicles.isEmpty(), "Vehicles should be seeded from CSV");
        assertTrue(vehicles.size() > 100, "CSV contains many unique vehicles");
    }

    @Test
    void testFindByNameFindsSeededVehicle() {
        Optional<Vehicle> found = vehicleRepository.findByName("McLaren MCL39");
        assertTrue(found.isPresent());
        assertEquals("McLaren MCL39", found.get().getName());
    }

    @Test
    void testFindByNameNotFound() {
        Optional<Vehicle> found = vehicleRepository.findByName("Nonexistent Car XYZ");
        assertFalse(found.isPresent());
    }

    @Test
    void testSaveNewVehicle() {
        long countBefore = vehicleRepository.count();
        Vehicle saved = vehicleRepository.save(new Vehicle("Unit Test Car 9000"));

        assertNotNull(saved.getVehicleId());
        assertEquals(countBefore + 1, vehicleRepository.count());
    }

    @Test
    void testUniqueNameConstraint() {
        // McLaren MCL39 already exists from CSV seeding
        assertThrows(Exception.class, () -> {
            vehicleRepository.save(new Vehicle("McLaren MCL39"));
            vehicleRepository.flush();
        });
    }
}
