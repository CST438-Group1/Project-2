package com.team1.f1_api.controller;

import com.team1.f1_api.model.Track;
import com.team1.f1_api.repository.TrackRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackRepository trackRepository;

    public TrackController(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @GetMapping
    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }
}
