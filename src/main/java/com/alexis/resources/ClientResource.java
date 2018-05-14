package com.alexis.resources;

import com.alexis.domains.Client;
import com.alexis.stores.ClientDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/clients")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ClientResource extends RestResource{

    @GET
    public Response getClients() {
        try {
            List<Client> clients = new ClientDAO(getConnection()).getClients();
            GenericEntity<List<Client>> entity = new GenericEntity<List<Client>>(clients) {};

            return Response.ok(entity).build();
        } catch (SQLException e) {
            throw new WebApplicationException(e);
        }
    }
}
