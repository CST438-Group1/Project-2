package com.team1.f1_api.config;

import com.team1.f1_api.model.FastestLap;
import com.team1.f1_api.model.Track;
import com.team1.f1_api.model.Vehicle;
import com.team1.f1_api.repository.FastestLapRepository;
import com.team1.f1_api.repository.TrackRepository;
import com.team1.f1_api.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class responsible for seeding the Supabase PostgreSQL database
 * with data from the fastestLaps CSV file on application startup.
 *
 * The loader reads the CSV from the classpath, parses each row, and creates
 * normalized records across three tables: tracks, vehicles, and fastest_laps.
 * It uses in-memory caches to deduplicate tracks and vehicles during import.
 *
 * The import only runs once — if the fastest_laps table already contains data,
 * the loader skips execution to avoid duplicating records.
 */
@Configuration
public class CsvDataLoader {

    private static final Logger log = LoggerFactory.getLogger(CsvDataLoader.class);

    /**
     * Creates a {@link CommandLineRunner} bean that seeds the database from the CSV file.
     * Runs automatically on application startup. Skips if data already exists.
     *
     * @param trackRepo   repository for persisting Track entities
     * @param vehicleRepo repository for persisting Vehicle entities
     * @param lapRepo     repository for persisting FastestLap entities
     * @return a CommandLineRunner that performs the CSV import
     */
    @Bean
    CommandLineRunner loadCsvData(TrackRepository trackRepo,
                                  VehicleRepository vehicleRepo,
                                  FastestLapRepository lapRepo) {
        return args -> {
            if (lapRepo.count() > 0) {
                log.info("Database already seeded — skipping CSV import.");
                return;
            }

            log.info("Seeding database from CSV...");

            ClassPathResource resource = new ClassPathResource("fastestLaps.csv");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            Map<String, Track> trackCache = new HashMap<>();
            Map<String, Vehicle> vehicleCache = new HashMap<>();

            String line;
            int lineNum = 0;
            int imported = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;

                // Skip header row and the decorative title row
                if (lineNum <= 2) continue;

                String[] cols = parseCsvLine(line);
                if (cols.length < 8) {
                    log.warn("Skipping malformed line {}: {}", lineNum, line);
                    continue;
                }

                String region = cols[0].trim();
                String country = cols[1].trim();
                String trackName = cols[2].trim();
                String layout = cols[3].trim();
                String lengthStr = cols[4].trim();
                String vehicleName = cols[5].trim();
                String lapTime = cols[6].trim();
                String driver = cols[7].trim();

                if (trackName.isEmpty() || vehicleName.isEmpty() || lapTime.isEmpty()) continue;

                Double parsedLength = null;
                try {
                    parsedLength = Double.parseDouble(lengthStr);
                } catch (NumberFormatException e) {
                    // leave null if not parseable
                }
                final Double lengthKm = parsedLength;

                // Get or create Track
                String trackKey = trackName + "|" + layout;
                Track track = trackCache.get(trackKey);
                if (track == null) {
                    track = trackRepo.findByNameAndLayout(trackName, layout)
                            .orElseGet(() -> trackRepo.save(new Track(region, country, trackName, layout, lengthKm)));
                    trackCache.put(trackKey, track);
                }

                // Get or create Vehicle
                Vehicle vehicle = vehicleCache.get(vehicleName);
                if (vehicle == null) {
                    vehicle = vehicleRepo.findByName(vehicleName)
                            .orElseGet(() -> vehicleRepo.save(new Vehicle(vehicleName)));
                    vehicleCache.put(vehicleName, vehicle);
                }

                // Create FastestLap
                lapRepo.save(new FastestLap(track, vehicle, lapTime, driver));
                imported++;

                if (imported % 2000 == 0) {
                    log.info("Imported {} lap records...", imported);
                }
            }

            reader.close();
            log.info("CSV import complete: {} lap records imported.", imported);
        };
    }

    /**
     * Parses a single CSV line into an array of field values.
     * Handles quoted fields that may contain commas and escaped double quotes.
     *
     * @param line a single line from the CSV file
     * @return an array of parsed field values
     */
    private String[] parseCsvLine(String line) {
        java.util.List<String> fields = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }
}
