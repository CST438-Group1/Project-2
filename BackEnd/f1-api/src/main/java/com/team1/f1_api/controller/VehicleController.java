package com.team1.f1_api.controller;

import com.team1.f1_api.model.Vehicle;
import com.team1.f1_api.repository.VehicleRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for vehicle-related API endpoints.
 * Exposes endpoints under /vehicles for retrieving vehicle data
 * stored in Supabase PostgreSQL.
 */
@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleRepository vehicleRepository;

    /**
     * Constructs the controller with the required VehicleRepository dependency.
     *
     * @param vehicleRepository the JPA repository for Vehicle entities
     */
    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Retrieves all vehicles from the database.
     *
     * @return a list of all Vehicle entities
     */
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * Retrieves a single vehicle by its ID.
     *
     * @param id the vehicle ID
     * @return the Vehicle entity, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return vehicleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Searches for a vehicle by exact name.
     *
     * @param name the vehicle name to search for
     * @return the matching Vehicle, or 404 if not found
     */
    @GetMapping("/search")
    public ResponseEntity<Vehicle> getVehicleByName(@RequestParam String name) {
        return vehicleRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
