package com.team1.f1_api.controller;

import com.team1.f1_api.model.Vehicle;
import com.team1.f1_api.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class VehicleControllerTest {
    // ── getAllVehicles ──────────────────────────────────────────────────────────

    @Test
    void testGetAllVehiclesReturnsListOfVehicles() {
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        List<Vehicle> expectedVehicles = Arrays.asList(
                new Vehicle("FakeCar1"),
                new Vehicle("FakeCar2")
        );
        when(mockRepo.findAll()).thenReturn(expectedVehicles);

        VehicleController controller = new VehicleController(mockRepo);
        List<Vehicle> result = controller.getAllVehicles();

        assertEquals(2, result.size());
        assertEquals("FakeCar1", result.get(0).getName());
        assertEquals("FakeCar2", result.get(1).getName());
        verify(mockRepo, times(1)).findAll();
    }

    @Test
    void testGetAllVehiclesReturnsEmptyList() {
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        when(mockRepo.findAll()).thenReturn(Collections.emptyList());

        VehicleController controller = new VehicleController(mockRepo);
        List<Vehicle> result = controller.getAllVehicles();

        assertTrue(result.isEmpty());
        verify(mockRepo, times(1)).findAll();
    }
    // ── getVehicleById ──────────────────────────────────────────────────────────

    @Test
    void testGetVehicleByIdReturns200WhenFound() {
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        Vehicle vehicle = new Vehicle("FakeCar1");
        when(mockRepo.findById(1L)).thenReturn(Optional.of(vehicle));

        VehicleController controller = new VehicleController(mockRepo);
        ResponseEntity<Vehicle> response = controller.getVehicleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("FakeCar1", response.getBody().getName());
    }

    @Test
    void testGetVehicleByIdReturns404WhenNotFound() {
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        when(mockRepo.findById(99L)).thenReturn(Optional.empty());

        VehicleController controller = new VehicleController(mockRepo);
        ResponseEntity<Vehicle> response = controller.getVehicleById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ── createVehicle ───────────────────────────────────────────────────────────

    @Test
    void testCreateVehicleReturns201AndSavedVehicle() {
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        Vehicle input = new Vehicle("FakeCar1");
        when(mockRepo.save(input)).thenReturn(input);

        VehicleController controller = new VehicleController(mockRepo);
        ResponseEntity<Vehicle> response = controller.createVehicle(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("FakeCar1", response.getBody().getName());
        verify(mockRepo, times(1)).save(input);
    }

    // ── updateVehicle ───────────────────────────────────────────────────────────

    @Test
    void testUpdateVehicleReturns200AndUpdatesExistingEntity() {
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        Vehicle existing = new Vehicle("FakeCar1");
        Vehicle updated = new Vehicle("FakeCar2");
        when(mockRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(mockRepo.save(existing)).thenReturn(existing);

        VehicleController controller = new VehicleController(mockRepo);
        ResponseEntity<Vehicle> response = controller.updateVehicle(1L, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(existing, response.getBody());
        assertEquals("FakeCar2", response.getBody().getName());
    }

    @Test
    void testUpdateVehicleReturns404WhenNotFound() {
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        when(mockRepo.findById(99L)).thenReturn(Optional.empty());

        VehicleController controller = new VehicleController(mockRepo);
        ResponseEntity<Vehicle> response = controller.updateVehicle(99L,
                new Vehicle("FakeCar1"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ── deleteVehicle ───────────────────────────────────────────────────────────

    @Test
    void testDeleteVehicleReturns204WhenFound() {
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        when(mockRepo.existsById(1L)).thenReturn(true);

        VehicleController controller = new VehicleController(mockRepo);
        ResponseEntity<Void> response = controller.deleteVehicle(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(mockRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteVehicleReturns404WhenNotFound() {
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        when(mockRepo.existsById(99L)).thenReturn(false);

        VehicleController controller = new VehicleController(mockRepo);
        ResponseEntity<Void> response = controller.deleteVehicle(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(mockRepo, never()).deleteById(any());
    }
    // ── getVehicleByName ────────────────────────────────────────────────────────

    @Test
    void testGetVehicleByNameReturns200WhenFound() {
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        Vehicle vehicle = new Vehicle("FakeCar1");
        when(mockRepo.findByName("FakeCar1")).thenReturn(Optional.of(vehicle));


        VehicleController controller = new VehicleController(mockRepo);
        ResponseEntity<Vehicle> response = controller.getVehicleByName("FakeCar1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("FakeCar1", response.getBody().getName());
    }
    @Test
    void testGetVehicleByNameReturns404WhenNotFound(){
        VehicleRepository mockRepo = mock(VehicleRepository.class);
        when(mockRepo.findByName("NonExistentCar")).thenReturn(Optional.empty());

        VehicleController controller = new VehicleController(mockRepo);
        ResponseEntity<Vehicle> response = controller.getVehicleByName("NonExistentCar");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
