package org.katastrofi.cs5k;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;

import static com.google.common.collect.Sets.newHashSet;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

public class FunctionalTest {

    @Test
    public void allCodeSetsCanBeQueried() {
        when()
                .get("/codesets")
        .then()
                .body(is("get all codesets"));
    }

    @Test
    public void codeSetCanBeQueriedByName() {
        when()
                .get("/codesets/CS01")
        .then()
                .body(is("get codeset CS01"));
    }

    @Test
    public void codeSetCanBeAddedByName() {
        given()
                .contentType(ContentType.JSON)
                .body(new CodeSet("CS01", "desc", newHashSet()))
        .when()
                .put("/codesets/CS01")
        .then()
                .body(is("add or update codeset CS01"));
    }

    @Test
    public void codeCanBeAddedByNameAndCodeSetName() {
        given()
                .contentType(ContentType.JSON)
                .body(new Code("C01", "desc", newHashSet()))
        .when()
                .put("/codesets/CS01/C01")
        .then()
                .body(is("add or update code C01 in codeset CS01"));
    }

    @Test
    public void codeSetCanBeDeletedByName() {
        when()
                .delete("/codesets/CS01")
        .then()
                .body(is("delete codeset CS01"));
    }

    @Test
    public void codeCanBeDeleteddByNameAndCodeSetName() {
        when()
                .delete("/codesets/CS01/C01")
        .then()
                .body(is("delete code C01 in codeset CS01"));
    }
}
