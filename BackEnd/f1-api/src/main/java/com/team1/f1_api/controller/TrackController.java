package com.team1.f1_api.controller;

import com.team1.f1_api.model.Track;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {
    @GetMapping
    public List<Track> getTracks() {
        return List.of(new Track("1", "Monza", "IT"));
    } //Hard coded data to return for now
}
