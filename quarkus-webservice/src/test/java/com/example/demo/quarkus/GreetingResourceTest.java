package com.example.demo.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testSimpleGreetingEndpoint() {
        given()
            .when().get("/api/greetings/simple/World")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("message", containsString("Hello World"))
                .body("status", equalTo("SUCCESS"))
                .body("processingMetadata.version", equalTo("1.0.0"));
    }

    @Test
    public void testComplexGreetingEndpoint() {
        String requestBody = """
            {
                "userContext": {
                    "userId": "user123",
                    "username": "John",
                    "email": "john@example.com",
                    "roles": ["USER", "ADMIN"],
                    "preferences": {}
                },
                "configuration": {
                    "language": "fr",
                    "includeTimestamp": false,
                    "uppercase": false,
                    "prefix": "Hey",
                    "suffix": "!",
                    "maxLength": 100
                },
                "tags": ["test", "demo"],
                "metadata": {"source": "test"},
                "priority": "HIGH",
                "timestamp": "2024-01-01T10:00:00"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/api/greetings/complex")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("message", containsString("Bonjour"))
                .body("status", equalTo("SUCCESS"))
                .body("appliedTransformations", hasItems("LANGUAGE_TRANSLATED", "PREFIX_APPLIED"));
    }
}
