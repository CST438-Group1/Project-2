package com.team1.f1_api.repository;

import com.team1.f1_api.model.Track;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrackRepository extends JpaRepository<Track, Long> {
    Optional<Track> findByNameAndLayout(String name, String layout);

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

    @Query(
        """
            select distinct t.country as country
            from Track t
            where t.region = :region
              and t.country is not null and t.country <> ''
            order by t.country
        """
    )
    List<CountryView> findCountriesByRegion(@Param("region") String region);
}
