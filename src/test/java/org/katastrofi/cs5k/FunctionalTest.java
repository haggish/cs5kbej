package org.katastrofi.cs5k;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;

import static com.google.common.collect.Sets.newHashSet;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.hamcrest.Matchers.is;

public class FunctionalTest {

    @Test
    public void allCodeSetsCanBeQueried() {
        when()
                .get("/codesets")
        .then()
                .body("[0].name", is("CS01"))
                .body("[0].codes[0].name", is("C01"));
    }

    @Test
    public void codeSetCanBeQueriedByName() {
        when()
                .get("/codesets/CS01")
        .then()
                .body("name", is("CS01"))
                .body("codes[0].name", is("C01"));
    }

    @Test
    public void codeSetCanBeAddedByName() {
        given()
                .contentType(ContentType.JSON)
                .body(new CodeSet("CS01", "desc", newHashSet()))
        .when()
                .put("/codesets/CS01")
        .then()
                .statusCode(is(SC_NO_CONTENT));
    }

    @Test
    public void codeSetCanBeUpdatedByName() {
        given()
                .contentType(ContentType.JSON)
                .body(new CodeSet("CS02", "desc", newHashSet()))
        .when()
                .put("/codesets/CS02")
        .then()
                .statusCode(is(SC_CREATED));
    }

    @Test
    public void codeCanBeAddedByNameAndCodeSetName() {
        given()
                .contentType(ContentType.JSON)
                .body(new Code("C01", "desc", newHashSet()))
        .when()
                .put("/codesets/CS01/C01")
        .then()
                .statusCode(is(SC_NO_CONTENT));
    }

    @Test
    public void codeCanBeUpdatedByNameAndCodeSetName() {
        given()
                .contentType(ContentType.JSON)
                .body(new Code("C02", "desc", newHashSet()))
        .when()
                .put("/codesets/CS01/C02")
        .then()
                .statusCode(is(SC_CREATED));
    }

    @Test
    public void codeSetCanBeDeletedByName() {
        when()
                .delete("/codesets/CS01")
        .then()
                .statusCode(is(SC_NO_CONTENT));
    }

    @Test
    public void codeCanBeDeletedByNameAndCodeSetName() {
        when()
                .delete("/codesets/CS01/C01")
        .then()
                .statusCode(is(SC_NO_CONTENT));
    }
}
