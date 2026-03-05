package com.team1.f1_api.controller;

import com.team1.f1_api.model.Track;
import com.team1.f1_api.repository.TrackRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for track-related API endpoints.
 * Exposes endpoints under /tracks for retrieving racing track data
 * stored in Supabase PostgreSQL.
 */
@RestController
@RequestMapping("/tracks")
@CrossOrigin(origins = "*")
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
    @GetMapping
    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    /**
     * Retrieves a single track by its ID.
     *
     * @param id the track ID
     * @return the Track entity, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Track> getTrackById(@PathVariable Long id) {
        return trackRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
