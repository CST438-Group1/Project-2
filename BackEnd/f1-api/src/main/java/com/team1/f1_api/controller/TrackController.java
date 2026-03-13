package com.team1.f1_api.controller;

import com.team1.f1_api.model.Track;
import com.team1.f1_api.repository.TrackRepository;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for track-related API endpoints.
 * Exposes endpoints under /tracks for retrieving racing track data
 * stored in Supabase PostgreSQL.
 */
@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackRepository trackRepository;

    /**
     * Constructs the controller with the required TrackRepository dependency.
     *
     * @param trackRepository the JPA repository for Track entities
     */
    public TrackController(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    /**
     * Retrieves all tracks from the database.
     *
     * @return a list of all Track entities
     */
    @Operation(summary = "Get all tracks", description = "Returns a list of all racing tracks")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved tracks")
    @GetMapping
    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    /**
     *  Retrieves a track by ID from the database.
     * @param id the track's primary key
     * @return the matching Track entity, or 404 if not found
     */
    @Operation(summary = "Get track by ID", description = "Returns a specific track's information")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved track")
    @ApiResponse(responseCode = "404", description = "Track not found")
    @GetMapping("/{id}")
    public ResponseEntity<Track> getTrackById(@PathVariable Long id) {
        return trackRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new track in the database.
     * @param track the track's information
     * @return the created Track entity with generated ID
     */
    @Operation(summary = "Create new track", description = "Creates new track")
    @ApiResponse(responseCode = "201", description = "Successfully created track")
    @PostMapping
    public ResponseEntity<Track> createTrack(@RequestBody Track track) {
        Track saved = trackRepository.save(track);
        return ResponseEntity.created(null).body(saved);
    }

    /**
     * Updates a track by ID in the database
     * @param id the track's primary key
     * @param updated the updated track information
     * @return the updated Track entity, or 404 if not found
     */
    @Operation(summary = "Updates track by ID", description = "Updates a specific track's information")
    @ApiResponse(responseCode = "200", description = "Successfully updated track")
    @ApiResponse(responseCode = "404", description = "Track not found")
    @PutMapping("/{id}")
    public ResponseEntity<Track> updateTrack(@PathVariable Long id, @RequestBody Track updated) {
        return trackRepository.findById(id)
                .map(existing -> {
                    existing.setRegion(updated.getRegion());
                    existing.setCountry(updated.getCountry());
                    existing.setName(updated.getName());
                    existing.setLayout(updated.getLayout());
                    existing.setLengthKm(updated.getLengthKm());
                    return ResponseEntity.ok(trackRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes track by ID in database.
     * @param id the track's primary key
     * @return 204 No Content on success, or 404 if not found
     */
    @Operation(summary = "Delete track by ID", description = "Deletes a specific track's information")
    @ApiResponse(responseCode = "204", description = "Successfully deleted track")
    @ApiResponse(responseCode = "404", description = "Track not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        if (!trackRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        trackRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
