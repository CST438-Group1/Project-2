package com.team1.f1_api.controller;

import com.team1.f1_api.controller.dto.ContinentDto;
import com.team1.f1_api.controller.dto.CountryDto;
import com.team1.f1_api.repository.TrackRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/continents")
public class ContinentController {

    private final TrackRepository trackRepository;

    public ContinentController(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Operation(summary = "Get all continents", description = "Retrieves all continents")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved continents")
    @GetMapping
    public List<ContinentDto> getContinents() {

        Map<String, Long> continentCounts = trackRepository
                .countCountriesByRegion()
                .stream()
                .collect(Collectors.groupingBy(
                        v -> mapRegionToName(v.getRegion()),
                        Collectors.summingLong(v -> v.getCountryCount())
                ));

        return continentCounts.entrySet()
                .stream()
                .map(entry -> new ContinentDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(ContinentDto::name))
                .toList();
    }

    @Operation(summary = "Get all countries", description = "Retrieves all countries by continent")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved countries")
    @ApiResponse(responseCode = "404", description = "Continent not found")
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
            case "OC", "AU" -> "Australia";
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
            case "AUSTRALIA" -> "AU";
            default -> null;
        };
    }
}