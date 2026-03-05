package com.team1.f1_api.controller;

import com.team1.f1_api.controller.dto.ContinentDto;
import com.team1.f1_api.controller.dto.CountryDto;
import com.team1.f1_api.repository.TrackRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/continents")
public class ContinentController {

    private final TrackRepository trackRepository;

    public ContinentController(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @GetMapping
    public List<ContinentDto> getContinents() {
        return trackRepository
            .countCountriesByRegion()
            .stream()
            .map(v ->
                new ContinentDto(
                    mapRegionToName(v.getRegion()),
                    v.getCountryCount()
                )
            )
            .sorted(Comparator.comparing(ContinentDto::name))
            .toList();
    }

    @GetMapping("/{continentName}/countries")
    public ResponseEntity<List<CountryDto>> getCountriesByContinent(
        @PathVariable String continentName
    ) {
        String region = mapNameToRegion(continentName);
        if (region == null) {
            return ResponseEntity.notFound().build();
        }

        List<CountryDto> countries = trackRepository
            .findCountriesByRegion(region)
            .stream()
            .map(v -> new CountryDto(v.getCountry()))
            .toList();

        return ResponseEntity.ok(countries);
    }

    private String mapRegionToName(String region) {
        if (region == null) return "Unknown";
        return switch (region.trim().toUpperCase()) {
            case "NA" -> "North America";
            case "SA" -> "South America";
            case "EU" -> "Europe";
            case "AF" -> "Africa";
            case "AS", "AP", "APAC" -> "APAC";
            case "OC", "AU" -> "Oceania";
            default -> region.trim();
        };
    }

    private String mapNameToRegion(String continentName) {
        if (continentName == null) return null;
        return switch (continentName.trim().toUpperCase()) {
            case "NORTH AMERICA" -> "NA";
            case "SOUTH AMERICA" -> "SA";
            case "EUROPE" -> "EU";
            case "AFRICA" -> "AF";
            case "APAC", "ASIA" -> "APAC";
            case "OCEANIA" -> "OC";
            default -> null;
        };
    }
}
