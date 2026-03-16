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
class AuthRestAssuredTest {

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
        userRepo.findByUsername("testuser").ifPresent(userRepo::delete);
    }

    // ── POST /auth/register ───────────────────────────────────────────────────

    @Test
    void testRegisterReturns200OnSuccess() {
        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "testuser",
                        "email", "testuser@test.com",
                        "password", "password123"
                ))
                .when().post("/auth/register")
                .then().statusCode(200)
                .body("message", equalTo("Registration successful"));
    }

    @Test
    void testRegisterReturns400OnDuplicateUsername() {
        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "testuser",
                        "email", "testuser@test.com",
                        "password", "password123"
                ))
                .post("/auth/register");

        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "testuser",
                        "email", "different@test.com",
                        "password", "password123"
                ))
                .when().post("/auth/register")
                .then().statusCode(400)
                .body("error", equalTo("Username is already taken"));
    }

    @Test
    void testRegisterReturns400OnDuplicateEmail() {
        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "testuser",
                        "email", "testuser@test.com",
                        "password", "password123"
                ))
                .post("/auth/register");

        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "differentuser",
                        "email", "testuser@test.com",
                        "password", "password123"
                ))
                .when().post("/auth/register")
                .then().statusCode(400)
                .body("error", equalTo("Email is already registered"));
    }

    // ── POST /auth/login ──────────────────────────────────────────────────────

    @Test
    void testLoginReturns200OnSuccess() {
        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "testuser",
                        "email", "testuser@test.com",
                        "password", "password123"
                ))
                .post("/auth/register");

        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "testuser",
                        "password", "password123"
                ))
                .when().post("/auth/login")
                .then().statusCode(200)
                .body("message", equalTo("Login successful"));
    }

    @Test
    void testLoginReturns401WithWrongPassword() {
        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "testuser",
                        "email", "testuser@test.com",
                        "password", "password123"
                ))
                .post("/auth/register");

        given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "testuser",
                        "password", "wrongpassword"
                ))
                .when().post("/auth/login")
                .then().statusCode(401);
    }

    // ── POST /auth/logout ─────────────────────────────────────────────────────

    @Test
    void testLogoutReturns200() {
        given()
                .when().post("/auth/logout")
                .then().statusCode(200)
                .body("message", equalTo("Logged out"));
    }
}