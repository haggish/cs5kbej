package org.katastrofi.cs5k;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.BadRequestException;
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
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@Singleton
@Path("codesets")
public class RESTEndpoint {

    private final Service service;


    @Inject
    public RESTEndpoint(final Service service) {
        this.service = service;
    }


    @GET
    @Produces(APPLICATION_JSON)
    public Set<CodeSet> allCodeSets() {
        return service.allCodeSets();
    }

    @GET
    @Path("/{name}")
    @Produces(APPLICATION_JSON)
    public Response codeSetWithName(@PathParam("name") String name) {
        Optional<CodeSet> possibleCodeSet = service.codeSetWithName(name);
        if (possibleCodeSet.isPresent()) {
            return ok(possibleCodeSet.get()).build();
        } else {
            return status(NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{name}")
    @Consumes(APPLICATION_JSON)
    public Response addOrUpdate(@PathParam("name") String codeSetNameInPath,
                                CodeSet codeSet) {
        crossCheck(codeSetNameInPath, codeSet.name());

        boolean update = service.addOrUpdate(codeSet);

        return update ? noContent().build() : status(CREATED).build();
    }

    @PUT
    @Path("/{codeSetName}/{codeName}")
    @Consumes(APPLICATION_JSON)
    public Response addOrUpdateCodeSetsCode(
            @PathParam("codeSetName") String codeSetName,
            @PathParam("codeName") String codeNameInPath,
            Code code) {
        crossCheck(code.name(), codeNameInPath);

        try {
            boolean update = service.addOrUpdateCodeSetsCode(codeSetName, code);

            return update ? noContent().build() : status(CREATED).build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e);
        }
    }

    @DELETE
    public Response clearCodeSets() {
        service.clearCodeSets();
        return status(NO_CONTENT).build();
    }

    @DELETE
    @Path("/{name}")
    public Response removeCodeSetWithName(@PathParam("name") String name) {
        service.removeCodeSetWithName(name);
        return status(NO_CONTENT).build();
    }

    @DELETE
    @Path("/{codeSetName}/{codeName}")
    public Response removeCodeSetsCode(
            @PathParam("codeSetName") String codeSetName,
            @PathParam("codeName") String codeName) {
        service.removeCodeSetsCode(codeSetName, codeName);
        return status(NO_CONTENT).build();
    }


    private void crossCheck(String resourceNameInPath, String resourceName) {
        if (!resourceNameInPath.equals(resourceName)) {
            throw new BadRequestException(
                    "Resource name in path does not " +
                            "match with the actual resource name");
        }
    }
}