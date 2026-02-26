package com.team1.f1_api.repository;

import com.team1.f1_api.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {}
