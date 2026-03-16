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
class ContinentsRestAssuredTest {

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

    // ── GET /continents ───────────────────────────────────────────────────────

    @Test
    void testGetContinentsReturns200() {
        given()
                .when().get("/continents")
                .then().statusCode(200)
                .body("$", is(not(empty())));
    }

    // ── GET /continents/{name}/countries ──────────────────────────────────────

    @Test
    void testGetCountriesByContinentReturns200ForValidContinent() {
        given()
                .when().get("/continents/{name}/countries", "Europe")
                .then().statusCode(200)
                .body("$", is(not(empty())));
    }

    @Test
    void testGetCountriesByContinentReturns404ForUnknownContinent() {
        given()
                .when().get("/continents/{name}/countries", "Fake Continent")
                .then().statusCode(404);
    }
}