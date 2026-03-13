package com.team1.f1_api.controller;

import com.team1.f1_api.model.Vehicle;
import com.team1.f1_api.repository.VehicleRepository;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for vehicle-related API endpoints.
 * Exposes endpoints under /vehicles for retrieving vehicle data
 * stored in Supabase PostgreSQL.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
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
    @Operation(summary = "Get all vehicles", description = "Retrieves all vehicles")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicles")
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
    @Operation(summary = "Gets vehicle by ID", description = "Gets a specific vehicle's information")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicle")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
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
    @Operation(summary = "Gets vehicle by name", description = "Gets a specific vehicle's information")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicle")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    @GetMapping("/search")
    public ResponseEntity<Vehicle> getVehicleByName(@RequestParam String name) {
        return vehicleRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a vehicle in the database.
     * @param vehicle the vehicle information
     * @return the created Vehicle entity with generated ID
     */
    @Operation(summary = "Creates new vehicle", description = "Creates a new vehicle")
    @ApiResponse(responseCode = "201", description = "Successfully created vehicle")
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        Vehicle saved = vehicleRepository.save(vehicle);
        return ResponseEntity.created(null).body(saved);
    }

    /**
     * Updates a vehicle's information in the database.
     * @param id the vehicle's primary key
     * @param updated the updated vehicle information
     * @return the updated Vehicle entity, or 404 if not found
     */
    @Operation(summary = "Updates vehicle by ID", description = "Updates a specific vehicle's information")
    @ApiResponse(responseCode = "200", description = "Successfully updated vehicle")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle updated) {
        return vehicleRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    return ResponseEntity.ok(vehicleRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a vehicle from the database.
     * @param id the vehicle's primary key
     * @return 204 No Content on success, or 404 if not found
     */
    @Operation(summary = "Delete vehicle by ID", description = "Deletes a specific vehicle's information")
    @ApiResponse(responseCode = "204", description = "Successfully deleted vehicle")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        if (!vehicleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vehicleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
