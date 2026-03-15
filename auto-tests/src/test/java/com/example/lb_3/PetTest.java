package com.example.lb_3;

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

public class PetTest {

    private static final String baseUrl = "https://petstore.swagger.io/v2";
    private static final String PET = "/pet";
    private static final String PET_ID = "/pet/{petId}";

    private long petId = 1222245L; 
    private String petName = "Vladislav Holobokov Pet"; 

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = io.restassured.parsing.Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyPostAction() {
        Map<String, ?> body = Map.of(
                "id", petId,
                "name", petName,
                "status", "available"
        );

        given().body(body)
                .post(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(petName));
    }

    @Test(dependsOnMethods = "verifyPostAction")
    public void verifyGetAction() {
        given().pathParam("petId", petId)
                .get(PET_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(petName));
    }

    @Test(dependsOnMethods = "verifyGetAction")
    public void verifyPutAction() {
        String updatedName = "V. Holobokov Updated Pet";
        
        Map<String, ?> body = Map.of(
                "id", petId,
                "name", updatedName,
                "status", "sold"
        );

        given().body(body)
                .put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(updatedName))
                .body("status", equalTo("sold"));
    }
}