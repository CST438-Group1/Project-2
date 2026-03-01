package com.team1.f1_api.model;

import jakarta.persistence.*;

/**
 * JPA entity representing a racing track.
 * Maps to the "tracks" table in Supabase PostgreSQL.
 * Each track is uniquely identified by the combination of its name and layout.
 * Data is sourced from the fastestLaps CSV dataset.
 */
@Entity
@Table(name = "tracks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "layout"})
})
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;

    private String region;

    private String country;

    private String name;

    private String layout;

    private Double lengthKm;

    /** Default no-arg constructor required by JPA. */
    public Track() {}

    /**
     * Constructs a new Track with all fields.
     *
     * @param region   the continent/region code (e.g. "AU", "EU")
     * @param country  the country where the track is located
     * @param name     the official track name (e.g. "Albert Park")
     * @param layout   the track layout/configuration (e.g. "Full Circuit")
     * @param lengthKm the track length in kilometers
     */
    public Track(String region, String country, String name, String layout, Double lengthKm) {
        this.region = region;
        this.country = country;
        this.name = name;
        this.layout = layout;
        this.lengthKm = lengthKm;
    }

    /** @return the auto-generated primary key */
    public Long getTrackId() {
        return trackId;
    }

    /** @return the continent/region code (e.g. "AU", "EU") */
    public String getRegion() {
        return region;
    }

    /** @return the country where the track is located */
    public String getCountry() {
        return country;
    }

    /** @return the official track name */
    public String getName() {
        return name;
    }

    /** @return the track layout/configuration name */
    public String getLayout() {
        return layout;
    }

    /** @return the track length in kilometers */
    public Double getLengthKm() {
        return lengthKm;
    }
}
