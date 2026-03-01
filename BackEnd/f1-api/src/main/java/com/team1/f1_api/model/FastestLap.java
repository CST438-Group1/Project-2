package com.team1.f1_api.model;

import jakarta.persistence.*;

/**
 * JPA entity representing a fastest lap record.
 * Maps to the "fastest_laps" table in Supabase PostgreSQL.
 * Each record links a {@link Track} and {@link Vehicle} to a lap time and driver.
 * Data is sourced from the fastestLaps CSV dataset.
 */
@Entity
@Table(name = "fastest_laps")
public class FastestLap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lapId;

    /** The track where the lap was recorded. */
    @ManyToOne
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    /** The vehicle used for the lap. */
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /** The lap time as a formatted string (e.g. "1:15.10"). */
    @Column(nullable = false)
    private String lapTime;

    /** The name of the driver who set the lap time. */
    private String driver;

    /** Default no-arg constructor required by JPA. */
    public FastestLap() {}

    /**
     * Constructs a new FastestLap record.
     *
     * @param track   the track where the lap was recorded
     * @param vehicle the vehicle used for the lap
     * @param lapTime the lap time string (e.g. "1:15.10")
     * @param driver  the driver's name
     */
    public FastestLap(Track track, Vehicle vehicle, String lapTime, String driver) {
        this.track = track;
        this.vehicle = vehicle;
        this.lapTime = lapTime;
        this.driver = driver;
    }

    /** @return the auto-generated primary key */
    public Long getLapId() {
        return lapId;
    }

    /** @return the track where the lap was recorded */
    public Track getTrack() {
        return track;
    }

    /** @return the vehicle used for the lap */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /** @return the formatted lap time string */
    public String getLapTime() {
        return lapTime;
    }

    /** @return the driver's name */
    public String getDriver() {
        return driver;
    }
}
