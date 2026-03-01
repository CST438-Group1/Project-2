package com.team1.f1_api.model;

import jakarta.persistence.*;

/**
 * JPA entity representing a vehicle (car).
 * Maps to the "vehicles" table in Supabase PostgreSQL.
 * Each vehicle is uniquely identified by its name (e.g. "McLaren MCL39").
 * Data is sourced from the fastestLaps CSV dataset.
 */
@Entity
@Table(name = "vehicles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @Column(unique = true, nullable = false)
    private String name;

    /** Default no-arg constructor required by JPA. */
    public Vehicle() {}

    /**
     * Constructs a new Vehicle with the given name.
     *
     * @param name the full vehicle name (e.g. "McLaren MCL39", "Ferrari SF25")
     */
    public Vehicle(String name) {
        this.name = name;
    }

    /** @return the auto-generated primary key */
    public Long getVehicleId() {
        return vehicleId;
    }

    /** @return the full vehicle name */
    public String getName() {
        return name;
    }
}
