package com.team1.f1_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tracks")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;

    private String name;

    private String shortName;

    private Double lengthKm;

    private String surfaceType;

    private Integer configCount;

    public Track() {}

    public Long getTrackId() {
        return trackId;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Double getLengthKm() {
        return lengthKm;
    }

    public String getSurfaceType() {
        return surfaceType;
    }

    public Integer getConfigCount() {
        return configCount;
    }
}
