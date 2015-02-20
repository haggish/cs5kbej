package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.util.StdConverter;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Set;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("codesets")
public class RESTEndpoint {

    @GET
    public String all() {
        return "get all codesets";
    }

    @GET
    @Path("/{name}")
    public String withName(@PathParam("name") String name) {
        return "get codeset " + name;
    }

    @PUT
    @Path("/{name}")
    @Consumes(APPLICATION_JSON)
    public String addOrUpdate(@PathParam("name") String name, CodeSet codeSet) {
        return "add or update codeset " + name;
    }

    @PUT
    @Path("/{codeSetName}/{codeName}")
    @Consumes(APPLICATION_JSON)
    public String addOrUpdateCodeSetsCode(
            @PathParam("codeSetName") String codeSetName,
            @PathParam("codeName") String codeName,
            Code code) {
        return "add or update code " + codeName + " in codeset " + codeSetName;
    }

    @DELETE
    public String clear() {
        return "delete all codesets";
    }

    @DELETE
    @Path("/{name}")
    public String removeWithName(@PathParam("name") String name) {
        return "delete codeset " + name;
    }

    @DELETE
    @Path("/{codeSetName}/{codeName}")
    public String removeCodeSetsCode(
            @PathParam("codeSetName") String codeSetName,
            @PathParam("codeName") String codeName) {
        return "delete code " + codeName + " in codeset " + codeSetName;
    }


    static final class ToCodeSet
            extends StdConverter<ToCodeSet.TempCodeSet, CodeSet> {
        @Override
        public CodeSet convert(TempCodeSet value) {
            return new CodeSet(value.name, value.description, value.codes);
        }

        static class TempCodeSet {
            public String name;
            public String description;
            public Set<Code> codes;
        }
    }

    static final class ToCode
            extends StdConverter<ToCode.TempCode, Code> {
        @Override
        public Code convert(TempCode value) {
            return new Code(value.name, value.description, value.values);
        }

        static class TempCode {
            public String name;
            public String description;
            public Set<String> values;
        }
    }
}