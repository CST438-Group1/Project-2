package com.team1.f1_api.repository;

import com.team1.f1_api.model.Vehicle;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Vehicle} entities.
 * Provides standard CRUD operations and custom query methods
 * for accessing vehicle data stored in Supabase PostgreSQL.
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * Finds a vehicle by its exact name.
     * Used during CSV import to avoid duplicate vehicle entries.
     *
     * @param name the full vehicle name (e.g. "McLaren MCL39")
     * @return an Optional containing the matching Vehicle, or empty if not found
     */
    Optional<Vehicle> findByName(String name);
}
