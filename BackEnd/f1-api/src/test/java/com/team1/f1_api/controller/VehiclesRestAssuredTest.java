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
class VehiclesRestAssuredTest {

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

    // ── GET /vehicles ─────────────────────────────────────────────────────────

    @Test
    void testGetAllVehiclesReturns200() {
        given()
                .when().get("/vehicles")
                .then().statusCode(200)
                .body("$", is(not(empty())));
    }

    // ── GET /vehicles/{id} ────────────────────────────────────────────────────

    @Test
    void testGetVehicleByIdReturns200WhenFound() {
        int vehicleId = given()
                .when().get("/vehicles")
                .then().extract()
                .path("[0].vehicleId");

        given()
                .when().get("/vehicles/{id}", vehicleId)
                .then().statusCode(200)
                .body("vehicleId", equalTo(vehicleId));
    }

    @Test
    void testGetVehicleByIdReturns404WhenNotFound() {
        given()
                .when().get("/vehicles/{id}", 999999)
                .then().statusCode(404);
    }

    // ── GET /vehicles/search ──────────────────────────────────────────────────

    @Test
    void testGetVehicleByNameReturns200WhenFound() {
        given()
                .param("name", "McLaren MCL39")
                .when().get("/vehicles/search")
                .then().statusCode(200)
                .body("name", equalTo("McLaren MCL39"));
    }

    @Test
    void testGetVehicleByNameReturns404WhenNotFound() {
        given()
                .param("name", "Nonexistent Car XYZ")
                .when().get("/vehicles/search")
                .then().statusCode(404);
    }

    // ── POST /vehicles ────────────────────────────────────────────────────────

    @Test
    void testCreateVehicleReturns201() {
        String cookie = getAdminSessionCookie();

        given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(Map.of("name", "Test Car 9000"))
                .when().post("/vehicles")
                .then().statusCode(201)
                .body("name", equalTo("Test Car 9000"));
    }

    @Test
    void testCreateVehicleReturns401WithoutAuth() {
        given().contentType(ContentType.JSON)
                .body(Map.of("name", "Test Car 9000"))
                .when().post("/vehicles")
                .then().statusCode(either(equalTo(401)).or(equalTo(302)).or(equalTo(403)));
    }

    // ── PUT /vehicles/{id} ────────────────────────────────────────────────────

    @Test
    void testUpdateVehicleReturns200WhenFound() {
        String cookie = getAdminSessionCookie();

        int vehicleId = given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(Map.of("name", "Update Me Car"))
                .when().post("/vehicles")
                .then().statusCode(201)
                .extract().path("vehicleId");

        given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(Map.of("name", "Updated Car"))
                .when().put("/vehicles/{id}", vehicleId)
                .then().statusCode(200)
                .body("name", equalTo("Updated Car"));
    }

    @Test
    void testUpdateVehicleReturns404WhenNotFound() {
        String cookie = getAdminSessionCookie();

        given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(Map.of("name", "Updated Car"))
                .when().put("/vehicles/{id}", 999999)
                .then().statusCode(404);
    }

    // ── DELETE /vehicles/{id} ─────────────────────────────────────────────────

    @Test
    void testDeleteVehicleReturns204WhenFound() {
        String cookie = getAdminSessionCookie();

        int vehicleId = given().contentType(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(Map.of("name", "Delete Me Car 9999"))
                .when().post("/vehicles")
                .then().statusCode(201)
                .extract().path("vehicleId");

        given().cookie("JSESSIONID", cookie)
                .when().delete("/vehicles/{id}", vehicleId)
                .then().statusCode(204);
    }

    @Test
    void testDeleteVehicleReturns404WhenNotFound() {
        String cookie = getAdminSessionCookie();

        given().cookie("JSESSIONID", cookie)
                .when().delete("/vehicles/{id}", 999999)
                .then().statusCode(404);
    }
}