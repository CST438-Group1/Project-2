-- ===============================
-- MotoRYX — Supabase PostgreSQL Schema
-- This script is executed automatically on Spring Boot startup
-- via spring.sql.init.mode=always in application.properties.
-- It can also be run manually in the Supabase SQL Editor.
-- All statements use IF NOT EXISTS to ensure safe re-runs.
-- ===============================

-- -----------------------------------------------
-- Table: tracks
-- Stores unique racing tracks identified by (name, layout).
-- Columns:
--   track_id  - Auto-generated primary key
--   region    - Continent/region code (e.g. "AU", "EU")
--   country   - Country where the track is located
--   name      - Official track name (e.g. "Albert Park")
--   layout    - Track configuration (e.g. "Full Circuit")
--   length_km - Track length in kilometers
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS tracks (
    track_id    BIGSERIAL PRIMARY KEY,
    region      VARCHAR(10),
    country     VARCHAR(100),
    name        VARCHAR(255) NOT NULL,
    layout      VARCHAR(255),
    length_km   DOUBLE PRECISION,
    UNIQUE (name, layout)
);

-- -----------------------------------------------
-- Table: vehicles
-- Stores unique vehicle names from the lap data.
-- Columns:
--   vehicle_id - Auto-generated primary key
--   name       - Full vehicle name (e.g. "McLaren MCL39"), must be unique
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id  BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE
);

-- -----------------------------------------------
-- Table: fastest_laps
-- Stores individual lap time records linking a track, vehicle, and driver.
-- Foreign keys reference the tracks and vehicles tables.
-- Columns:
--   lap_id     - Auto-generated primary key
--   track_id   - FK to tracks(track_id)
--   vehicle_id - FK to vehicles(vehicle_id)
--   lap_time   - Formatted lap time string (e.g. "1:15.10")
--   driver     - Name of the driver who set the lap time
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS fastest_laps (
    lap_id      BIGSERIAL PRIMARY KEY,
    track_id    BIGINT NOT NULL REFERENCES tracks(track_id),
    vehicle_id  BIGINT NOT NULL REFERENCES vehicles(vehicle_id),
    lap_time    VARCHAR(20) NOT NULL,
    driver      VARCHAR(255)
);

-- Indexes for common query patterns (filter by track, vehicle, or driver)
CREATE INDEX IF NOT EXISTS idx_fastest_laps_track ON fastest_laps(track_id);
CREATE INDEX IF NOT EXISTS idx_fastest_laps_vehicle ON fastest_laps(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_fastest_laps_driver ON fastest_laps(driver);
