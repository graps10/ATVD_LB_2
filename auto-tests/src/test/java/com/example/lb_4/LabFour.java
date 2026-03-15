package com.example.lb_4;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LabFour {

    private static final String baseUrl = "https://13259711-e3fd-461f-ac2e-fe49a5d695c2.mock.pstmn.io";
    private static final String STUDENT_ENDPOINT = "/student";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = io.restassured.parsing.Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    // Test 1
    @Test
    public void verifyGetStudent() {
        given()
            .get(STUDENT_ENDPOINT)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("name", equalTo("Vladislav Holobokov"))
            .body("group", equalTo("122-22-4"));
    }

    // Test 2
    @Test
    public void verifyPostStudent() {
        Map<String, String> body = Map.of("info", "test data");

        given()
            .body(body)
            .post(STUDENT_ENDPOINT)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("status", equalTo("success"))
            .body("message", equalTo("Student added"));
    }
}