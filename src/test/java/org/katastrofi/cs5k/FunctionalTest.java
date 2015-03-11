package org.katastrofi.cs5k;

import com.google.common.collect.Range;
import com.jayway.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
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

    private CodeSet cs01;

    private Code c01;

    @Before
    public void init() {
        Map<Range<LocalDateTime>, String> values = newHashMap();
        values.put(Range.all(), "aValue");
        c01 = new Code("C01", "otherdesc", values);
        cs01 = new CodeSet("CS01", "desc", newHashSet(c01));

        delete("/codesets");
        given()
                .contentType(ContentType.JSON)
                .body(cs01)
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
                .body("[0].name", is(cs01.name()))
                .body("[0].codes[0].name", is(c01.name()));
    }

    @Test
    public void codeSetCanBeQueriedByName() {
        when()
                .get("/codesets/{name}", cs01.name())
        .then()
                .body("name", is(cs01.name()))
                .body("codes[0].name", is(c01.name()));
    }

    @Test
    public void codeSetWithNonExistentNameReturnsNotFound() {
        when()
                .get("/codesets/CS03")
        .then()
                .statusCode(is(SC_NOT_FOUND));
    }

    @Test
    public void codeSetCanBeUpdatedByName() {
        given()
                .contentType(ContentType.JSON)
                .body(new CodeSet(cs01.name(), "newdesc", cs01.codes()))
        .when()
                .put("/codesets/{name}", cs01.name())
        .then()
                .statusCode(is(SC_NO_CONTENT));
    }

    @Test
    public void codeSetCanBeAddedByName() {
        given()
                .contentType(ContentType.JSON)
                .body(new CodeSet("CS02", "otherdesc", newHashSet()))
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
    public void codeCanBeUpdatedByNameAndCodeSetName() {
        given()
                .contentType(ContentType.JSON)
                .body(new Code(c01.name(), "newdesc", c01.codevalues()))
        .when()
                .put("/codesets/{csid}/{cid}", cs01.name(), c01.name())
        .then()
                .statusCode(is(SC_NO_CONTENT));
    }

    @Test
    public void codeCanBeAddedByNameAndCodeSetName() {
        given()
                .contentType(ContentType.JSON)
                .body(new Code("C02", "desc", newHashMap()))
        .when()
                .put("/codesets/{csid}/C02", cs01.name())
        .then()
                .statusCode(is(SC_CREATED));
    }

    @Test
    public void addingCodeWithInconsistentNameIsBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body(new Code("C01", "desc", newHashMap()))
        .when()
                .put("/codesets/CS01/C03")
        .then()
                .statusCode(is(SC_BAD_REQUEST));
    }

    @Test
    public void addingCodeToNonExistentCodeSetIsBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body(new Code("C01", "desc", newHashMap()))
        .when()
                .put("/codesets/CS03/C01")
        .then()
                .statusCode(is(SC_BAD_REQUEST));
    }

    @Test
    public void codeSetCanBeDeletedByName() {
        when()
                .delete("/codesets/{id}", cs01.name())
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
                .delete("/codesets/{csid}/{cid}", cs01.name(), c01.name())
        .then()
                .statusCode(is(SC_NO_CONTENT));
    }

    @Test
    public void deletingNonExistentCodeInExistingCodeSetIsOK() {
        when()
                .delete("/codesets/{csid}/C03", cs01.name())
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
