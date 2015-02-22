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


    static class TempCodeSet {
        public String name;
        public String description;
        public Set<Code> codes;
    }

    static class TempCode {
        public String name;
        public String description;
        public Set<String> values;
    }

    static final class ToCodeSet
            extends StdConverter<TempCodeSet, CodeSet> {
        @Override
        public CodeSet convert(TempCodeSet value) {
            return new CodeSet(value.name, value.description, value.codes);
        }
    }

    static final class FromCodeSet
            extends StdConverter<CodeSet, TempCodeSet> {
        @Override
        public TempCodeSet convert(CodeSet value) {
            TempCodeSet temp = new TempCodeSet();
            temp.name = value.name();
            temp.description = value.description();
            temp.codes = value.codes();
            return temp;
        }
    }

    static final class ToCode
            extends StdConverter<TempCode, Code> {
        @Override
        public Code convert(TempCode value) {
            return new Code(value.name, value.description, value.values);
        }
    }

    static final class FromCode extends StdConverter<Code, TempCode> {
        @Override
        public TempCode convert(Code value) {
            TempCode temp = new TempCode();
            temp.name = value.name();
            temp.description = value.description();
            temp.values = value.values();
            return temp;
        }
    }
}