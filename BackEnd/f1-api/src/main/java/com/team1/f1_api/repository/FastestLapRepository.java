package com.team1.f1_api.repository;

import com.team1.f1_api.model.FastestLap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link FastestLap} entities.
 * Provides standard CRUD operations and custom query methods
 * for accessing lap time data stored in Supabase PostgreSQL.
 */
public interface FastestLapRepository extends JpaRepository<FastestLap, Long> {

    /**
     * Finds all fastest lap records for a given track.
     *
     * @param trackId the primary key of the track
     * @return list of FastestLap records associated with the track
     */
    List<FastestLap> findByTrackTrackId(Long trackId);

    /**
     * Finds all fastest lap records for a given vehicle.
     *
     * @param vehicleId the primary key of the vehicle
     * @return list of FastestLap records associated with the vehicle
     */
    List<FastestLap> findByVehicleVehicleId(Long vehicleId);

    /**
     * Finds all fastest lap records by a specific driver name.
     *
     * @param driver the driver's name
     * @return list of FastestLap records set by the driver
     */
    List<FastestLap> findByDriver(String driver);
}
