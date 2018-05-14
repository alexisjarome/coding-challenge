package com.alexis.resources;

import com.alexis.domains.Environment;
import com.alexis.exceptions.NotFoundException;
import com.alexis.stores.ApplicationDAO;
import com.alexis.stores.EnvironmentDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/environments")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class EnvironmentResource extends RestResource {

    @GET
    public Response getAll() {
        List<Environment> environments = new ArrayList<Environment>();
        Connection connection = getConnection();

        try {
            environments = new EnvironmentDAO(connection).getEnvironmentList();

            GenericEntity<List<Environment>> entity = new GenericEntity<List<Environment>>(environments) {};
            return Response.ok(entity).build();

        } catch (SQLException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Path("{id : [0-9]+(,[0-9]+)*}")
    public Response get(@PathParam("id") Long id, @QueryParam("detailed") boolean detailed) {
        Connection connection = getConnection();
        try {
            Environment environment = new EnvironmentDAO(connection).getEnvironment(id);

            if (environment == null) {
                throw new NotFoundException("environment " + id + " not found");
            }

            if (detailed) {
                environment.setApplications(new ApplicationDAO(connection).getApplicationsByEnvironment(id));
            }

            return Response.ok(environment).build();
        } catch (SQLException e) {
            throw new WebApplicationException(e);
        }
    }

    @Path("{id : [0-9]+(,[0-9]+)*}/applications")
    public ApplicationResource delegateToIPWhitelistResource(@PathParam("id") long id) {
        return new ApplicationResource(id, context);
    }




}
