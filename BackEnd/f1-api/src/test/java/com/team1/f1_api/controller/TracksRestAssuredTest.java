package com.team1.f1_api.controller;

import com.team1.f1_api.repository.AppUserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
class TracksRestAssuredTest {

    static {
        System.setProperty("java.net.useSystemProxies", "false");
    }

    @LocalServerPort
    private int port;

    @Autowired
    private AppUserRepository userRepo;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    @AfterEach
    void tearDown() {
        userRepo.findByUsername("adminuser").ifPresent(userRepo::delete);
    }

    private String getAdminSessionCookie() {
        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "adminuser",
                        "email", "admin@test.com",
                        "password", "password123"
                ))
                .post("/auth/register");

        userRepo.findByUsername("adminuser").ifPresent(user -> {
            user.setRole("ADMIN");
            userRepo.save(user);
        });

        return given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "adminuser", "password", "password123"))
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .cookie("JSESSIONID");
    }

    // ── GET /tracks ───────────────────────────────────────────────────────────

    @Test
    void testGetAllTracksReturns200() {
        given()
                .when().get("/tracks")
                .then().statusCode(200)
                .body("$", is(not(empty())));
    }

    // ── GET /tracks/{id} ──────────────────────────────────────────────────────

    @Test
    void testGetTrackByIdReturns200WhenFound() {
        int trackId = given()
                .when().get("/tracks")
                .then().extract()
                .path("[0].trackId");

        given()
                .when().get("/tracks/{id}", trackId)
                .then().statusCode(200)
                .body("trackId", equalTo(trackId));
    }

    @Test
    void testGetTrackByIdReturns404WhenNotFound() {
        given()
                .when().get("/tracks/{id}", 999999)
                .then().statusCode(404);
    }

    // ── POST /tracks ──────────────────────────────────────────────────────────

    @Test
    void testCreateTrackReturns201() {
        String cookie = getAdminSessionCookie();

        given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(Map.of(
                        "region", "EU",
                        "country", "Italy",
                        "name", "Test Circuit",
                        "layout", "Full Circuit",
                        "lengthKm", 5.0
                ))
                .when().post("/tracks")
                .then().statusCode(201)
                .body("name", equalTo("Test Circuit"));
    }

    @Test
    void testCreateTrackReturns401WithoutAuth() {
        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "region", "EU",
                        "country", "Italy",
                        "name", "Test Circuit",
                        "layout", "Full Circuit",
                        "lengthKm", 5.0
                ))
                .when().post("/tracks")
                .then().statusCode(either(equalTo(401)).or(equalTo(302)).or(equalTo(403)));
    }

    // ── PUT /tracks/{id} ──────────────────────────────────────────────────────

    @Test
    void testUpdateTrackReturns200WhenFound() {
        String cookie = getAdminSessionCookie();

        int trackId = given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(Map.of(
                        "region", "TEST",
                        "country", "Testland",
                        "name", "Update Me Circuit",
                        "layout", "Test Layout",
                        "lengthKm", 3.0
                ))
                .when().post("/tracks")
                .then().statusCode(201)
                .extract().path("trackId");

        given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(Map.of(
                        "region", "EU",
                        "country", "Italy",
                        "name", "Updated Circuit",
                        "layout", "Full Circuit",
                        "lengthKm", 4.5
                ))
                .when().put("/tracks/{id}", trackId)
                .then().statusCode(200)
                .body("name", equalTo("Updated Circuit"));
    }

    @Test
    void testUpdateTrackReturns404WhenNotFound() {
        String cookie = getAdminSessionCookie();

        given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(Map.of(
                        "region", "EU",
                        "country", "Italy",
                        "name", "Updated Circuit",
                        "layout", "Full Circuit",
                        "lengthKm", 4.5
                ))
                .when().put("/tracks/{id}", 999999)
                .then().statusCode(404);
    }

    // ── DELETE /tracks/{id} ───────────────────────────────────────────────────

    @Test
    void testDeleteTrackReturns204WhenFound() {
        String cookie = getAdminSessionCookie();

        int trackId = given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(Map.of(
                        "region", "TEST",
                        "country", "Testland",
                        "name", "Delete Me Circuit",
                        "layout", "Test Layout",
                        "lengthKm", 1.0
                ))
                .when().post("/tracks")
                .then().statusCode(201)
                .extract().path("trackId");

        given().cookie("JSESSIONID", cookie)
                .when().delete("/tracks/{id}", trackId)
                .then().statusCode(204);
    }

    @Test
    void testDeleteTrackReturns404WhenNotFound() {
        String cookie = getAdminSessionCookie();

        given().cookie("JSESSIONID", cookie)
                .when().delete("/tracks/{id}", 999999)
                .then().statusCode(404);
    }
}