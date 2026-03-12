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

    @GetMapping("/{id}")
    public ResponseEntity<Track> getTrackById(@PathVariable Long id) {
        return trackRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Track> createTrack(@RequestBody Track track) {
        Track saved = trackRepository.save(track);
        return ResponseEntity.ok(saved);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        if (!trackRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        trackRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
