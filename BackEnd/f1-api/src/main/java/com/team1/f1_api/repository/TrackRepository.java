package com.team1.f1_api.repository;

import com.team1.f1_api.model.Track;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for Track entities.
 * Provides CRUD operations and custom queries for Track data.
 */
public interface TrackRepository extends JpaRepository<Track, Long> {
    /**
     * Finds a track by its name and layout.
     * Used during CSV import to prevent duplicate track records.
     *
     * @param name   the track name
     * @param layout the track layout/configuration
     * @return an Optional containing the Track if found
     */
    Optional<Track> findByNameAndLayout(String name, String layout);

    /**
     * Groups tracks by region and counts distinct countries.
     * Used by the /continents endpoint.
     *
     * @return list of region + country count projections
     */
    @Query(
        """
            select t.region as region,
                   count(distinct t.country) as countryCount
            from Track t
            where t.region is not null and t.region <> ''
              and t.country is not null and t.country <> ''
            group by t.region
        """
    )
    List<ContinentCountView> countCountriesByRegion();
}
