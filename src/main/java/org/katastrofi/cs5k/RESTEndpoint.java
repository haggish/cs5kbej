package org.katastrofi.cs5k;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@Path("codesets")
public final class RESTEndpoint {

    private final CodeSets codeSets;


    @Inject
    public RESTEndpoint(final CodeSets codeSets) {
        this.codeSets = codeSets;
    }


    @GET
    @Produces(APPLICATION_JSON)
    public Set<CodeSet> all() {
        return codeSets.all();
    }

    @GET
    @Path("/{name}")
    @Produces(APPLICATION_JSON)
    public Response withName(@PathParam("name") String name) {
        Optional<CodeSet> possibleCodeSet = codeSets.withName(name);
        if (possibleCodeSet.isPresent()) {
            return ok(possibleCodeSet.get()).build();
        } else {
            return status(NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{name}")
    @Consumes(APPLICATION_JSON)
    public Response addOrUpdate(@PathParam("name") String codeSetName,
                                CodeSet codeSet) {
        if (!codeSet.name().equals(codeSetName)) {
            return status(BAD_REQUEST).build();
        }

        boolean update = codeSets.addOrUpdate(codeSet);

        return update ? noContent().build() : status(CREATED).build();
    }

    @PUT
    @Path("/{codeSetName}/{codeName}")
    @Consumes(APPLICATION_JSON)
    public Response addOrUpdateCodeSetsCode(
            @PathParam("codeSetName") String codeSetName,
            @PathParam("codeName") String codeName,
            Code code) {

        if (!code.name().equals(codeName)) {
            return status(BAD_REQUEST).build();
        }
        Optional<CodeSet> possibleCodeSet = codeSets.withName(codeSetName);
        if (possibleCodeSet.isPresent()) {
            CodeSet codeSet = possibleCodeSet.get();
            boolean update = codeSet.addOrUpdate(code);
            return update ? noContent().build() : status(CREATED).build();
        } else {
            return status(BAD_REQUEST).build();
        }
    }

    @DELETE
    public Response clear() {
        codeSets.removeAll();
        return status(NO_CONTENT).build();
    }

    @DELETE
    @Path("/{name}")
    public Response removeWithName(@PathParam("name") String name) {
        codeSets.removeWithName(name);
        return status(NO_CONTENT).build();
    }

    @DELETE
    @Path("/{codeSetName}/{codeName}")
    public Response removeCodeSetsCode(
            @PathParam("codeSetName") String codeSetName,
            @PathParam("codeName") String codeName) {
        Optional<CodeSet> possibleCodeSet = codeSets.withName(codeSetName);
        if (possibleCodeSet.isPresent()) {
            possibleCodeSet.get().remove(codeName);
        }
        return status(NO_CONTENT).build();
    }
}