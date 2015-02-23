package org.katastrofi.cs5k;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.status;

@Path("codesets")
public class RESTEndpoint {

    private final Set<CodeSet> all =
            newHashSet(new CodeSet("CS01", "This is a demo codeset",
                    newHashSet(new Code("C01", "This is a demo code",
                            newHashSet("aValue")))));

    @GET
    @Produces(APPLICATION_JSON)
    public Set<CodeSet> all() {
        return all;
    }

    @GET
    @Path("/{name}")
    @Produces(APPLICATION_JSON)
    public CodeSet withName(@PathParam("name") String name) {
        return all.iterator().next();
    }

    @PUT
    @Path("/{name}")
    @Consumes(APPLICATION_JSON)
    public Response addOrUpdate(@PathParam("name") String name, CodeSet codeSet) {
        if (existing(name)) {
            return noContent().build();
        } else {
            return status(CREATED).build();
        }
    }

    @PUT
    @Path("/{codeSetName}/{codeName}")
    @Consumes(APPLICATION_JSON)
    public Response addOrUpdateCodeSetsCode(
            @PathParam("codeSetName") String codeSetName,
            @PathParam("codeName") String codeName,
            Code code) {
        if (existing(codeSetName)) {
            if (codeInSet(codeName, codeSetName)) {
                return noContent().build();
            } else {
                return status(CREATED).build();
            }
        } else {
            return status(BAD_REQUEST).build();
        }
    }

    @DELETE
    public Response clear() {
        return status(NO_CONTENT).build();
    }

    @DELETE
    @Path("/{name}")
    public Response removeWithName(@PathParam("name") String name) {
        return status(NO_CONTENT).build();
    }

    @DELETE
    @Path("/{codeSetName}/{codeName}")
    public Response removeCodeSetsCode(
            @PathParam("codeSetName") String codeSetName,
            @PathParam("codeName") String codeName) {
        return status(NO_CONTENT).build();
    }


    private boolean existing(String name) {
        return "CS01".equals(name);
    }

    private boolean codeInSet(String codeName, String codeSetName) {
        return "C01".equals(codeName);
    }
}