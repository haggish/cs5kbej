package org.katastrofi.cs5k;

import com.jayway.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Sets.newHashSet;
import static com.jayway.restassured.RestAssured.delete;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.hamcrest.Matchers.is;

public class FunctionalTest {

    @Before
    public void init() {
        given()
                .contentType(ContentType.JSON)
                .body(new CodeSet("CS01", "desc",
                        newHashSet(new Code("C01", "otherdesc",
                                newHashSet("aValue")))))
        .put("/codesets/CS01");
    }

    @After
    public void tearDown() {
        delete("/codesets");
    }

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
    public void codeSetWithNonExistentNameReturnsNotFound() {
        when()
                .get("/codesets/CS03")
        .then()
                .statusCode(is(SC_NOT_FOUND));
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
    public void codeSetWithInconsistentNameIsBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body(new CodeSet("CS01", "desc", newHashSet()))
                .when()
                .put("/codesets/CS03")
                .then()
                .statusCode(is(SC_BAD_REQUEST));
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
    public void addingCodeWithInconsistentNameIsBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body(new Code("C01", "desc", newHashSet()))
        .when()
                .put("/codesets/CS01/C03")
        .then()
                .statusCode(is(SC_BAD_REQUEST));
    }

    @Test
    public void addingCodeToNonExistentCodeSetIsBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body(new Code("C01", "desc", newHashSet()))
        .when()
                .put("/codesets/CS03/C01")
        .then()
                .statusCode(is(SC_BAD_REQUEST));
    }

    @Test
    public void codeSetCanBeDeletedByName() {
        when()
                .delete("/codesets/CS01")
                .then()
                .statusCode(is(SC_NO_CONTENT));
    }

    @Test
    public void deletingNonExistentCodeSetIsOK() {
        when()
                .delete("/codesets/CS03")
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

    @Test
    public void deletingNonExistentCodeInExistingCodeSetIsOK() {
        when()
                .delete("/codesets/CS01/C03")
        .then()
                .statusCode(is(SC_NO_CONTENT));
    }

    @Test
    public void deletingNonExistentCodeInNonExistentCodeSetIsBadRequest() {
        when()
                .delete("/codesets/CS03/C01")
        .then()
                .statusCode(is(SC_NO_CONTENT));
    }
}
