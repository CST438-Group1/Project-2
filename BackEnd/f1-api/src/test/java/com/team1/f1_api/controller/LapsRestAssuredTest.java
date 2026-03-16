package com.team1.f1_api.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
class LapsRestAssuredTest {

    static {
        System.setProperty("java.net.useSystemProxies", "false");
    }

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    // ── GET /laps ─────────────────────────────────────────────────────────────

    @Test
    void testGetAllLapsReturns200() {
        given()
                .when().get("/laps")
                .then().statusCode(200)
                .body("$", is(not(empty())));
    }

    // ── GET /laps/{id} ────────────────────────────────────────────────────────

    @Test
    void testGetLapByIdReturns200WhenFound() {
        int lapId = given()
                .when().get("/laps")
                .then().extract()
                .path("[0].lapId");

        given()
                .when().get("/laps/{id}", lapId)
                .then().statusCode(200)
                .body("lapId", equalTo(lapId));
    }

    @Test
    void testGetLapByIdReturns404WhenNotFound() {
        given()
                .when().get("/laps/{id}", 999999)
                .then().statusCode(404);
    }

    // ── GET /laps/track/{trackId} ─────────────────────────────────────────────

    @Test
    void testGetLapsByTrackReturns200() {
        int trackId = given()
                .when().get("/tracks")
                .then().extract()
                .path("[0].trackId");

        given()
                .when().get("/laps/track/{trackId}", trackId)
                .then().statusCode(200)
                .body("$", is(not(empty())));
    }

    // ── GET /laps/vehicle/{vehicleId} ─────────────────────────────────────────

    @Test
    void testGetLapsByVehicleReturns200() {
        int vehicleId = given()
                .when().get("/vehicles")
                .then().extract()
                .path("[0].vehicleId");

        given()
                .when().get("/laps/vehicle/{vehicleId}", vehicleId)
                .then().statusCode(200)
                .body("$", is(not(empty())));
    }

    // ── GET /laps/driver ──────────────────────────────────────────────────────

    @Test
    void testGetLapsByDriverReturns200WhenFound() {
        given()
                .param("driver", "Lando Norris")
                .when().get("/laps/driver")
                .then().statusCode(200)
                .body("$", is(not(empty())));
    }

    @Test
    void testGetLapsByDriverReturnsEmptyWhenNotFound() {
        given()
                .param("driver", "Unknown Driver XYZ")
                .when().get("/laps/driver")
                .then().statusCode(200)
                .body("$", hasSize(0));
    }
}