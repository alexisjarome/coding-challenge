package com.alexis.resources;

import com.alexis.domains.Application;
import com.alexis.domains.Environment;
import com.alexis.exceptions.NotFoundException;
import com.alexis.stores.ApplicationDAO;
import com.alexis.stores.EnvironmentDAO;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/applications")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ApplicationResource extends RestResource {

    private long environmentId;

    public ApplicationResource() {

    }

    public ApplicationResource(long environmentId, ServletContext servletContext) {
        super(servletContext);
        this.environmentId = environmentId;
    }

    @GET
    public Response getApplications() {
        Connection connection = getConnection();
        ApplicationDAO applicationDAO = new ApplicationDAO(connection);
        List<Application> applications = new ArrayList<Application>();

        try {
            if (environmentId == 0) {
                applications = applicationDAO.getApplicationList();
            } else {
                Environment environment = new EnvironmentDAO(connection).getEnvironment(environmentId);

                if (environment == null) {
                    throw new NotFoundException("environment " + environmentId + " not found");
                }

                applications = applicationDAO.getApplicationsByEnvironment(environment.getId());
            }
        } catch (SQLException e) {
            throw new WebApplicationException(e);
        }

        GenericEntity<List<Application>> entity = new GenericEntity<List<Application>>(applications) {};
        return Response.ok(entity).build();
    }

    @GET
    @Path("{id : [0-9]+(,[0-9]+)*}")
    public Response get(@PathParam("id") Long id) {
        Connection connection = getConnection();
        try {
            Application application = new ApplicationDAO(connection).getApplication(id);

            if (application == null) {
                throw new NotFoundException("application " + id + " not found");
            }

            return Response.ok(application).build();
        } catch (SQLException e) {
            throw new WebApplicationException(e);
        }
    }

    @Path("{id : [0-9]+(,[0-9]+)*}/ipwhitelists")
    public IPWhitelistResource delegateToApplicationResource(@PathParam("id") long id) {
        return new IPWhitelistResource(environmentId, id, context);
    }
}
