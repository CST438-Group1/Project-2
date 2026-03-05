package com.team1.f1_api.controller;

import com.team1.f1_api.model.FastestLap;
import com.team1.f1_api.repository.FastestLapRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for fastest-lap-related API endpoints.
 * Exposes endpoints under /laps for retrieving lap time records
 * stored in Supabase PostgreSQL. Supports filtering by track, vehicle, or driver.
 */
@RestController
@RequestMapping("/laps")
@CrossOrigin(origins = "*")
public class FastestLapController {

    private final FastestLapRepository fastestLapRepository;

    /**
     * Constructs the controller with the required FastestLapRepository dependency.
     *
     * @param fastestLapRepository the JPA repository for FastestLap entities
     */
    public FastestLapController(FastestLapRepository fastestLapRepository) {
        this.fastestLapRepository = fastestLapRepository;
    }

    /**
     * Retrieves all fastest lap records from the database.
     *
     * @return a list of all FastestLap entities
     */
    @GetMapping
    public List<FastestLap> getAllLaps() {
        return fastestLapRepository.findAll();
    }

    /**
     * Retrieves a single lap record by its ID.
     *
     * @param id the lap ID
     * @return the FastestLap entity, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<FastestLap> getLapById(@PathVariable Long id) {
        return fastestLapRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all lap records for a specific track.
     *
     * @param trackId the track's primary key
     * @return a list of FastestLap records for the given track
     */
    @GetMapping("/track/{trackId}")
    public List<FastestLap> getLapsByTrack(@PathVariable Long trackId) {
        return fastestLapRepository.findByTrackTrackId(trackId);
    }

    /**
     * Retrieves all lap records for a specific vehicle.
     *
     * @param vehicleId the vehicle's primary key
     * @return a list of FastestLap records for the given vehicle
     */
    @GetMapping("/vehicle/{vehicleId}")
    public List<FastestLap> getLapsByVehicle(@PathVariable Long vehicleId) {
        return fastestLapRepository.findByVehicleVehicleId(vehicleId);
    }

    /**
     * Retrieves all lap records by a specific driver name.
     *
     * @param driver the driver's name
     * @return a list of FastestLap records set by the driver
     */
    @GetMapping("/driver")
    public List<FastestLap> getLapsByDriver(@RequestParam String driver) {
        return fastestLapRepository.findByDriver(driver);
    }
}
