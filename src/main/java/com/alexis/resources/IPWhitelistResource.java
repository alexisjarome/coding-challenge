package com.alexis.resources;

import com.alexis.domains.IPAddress;
import com.alexis.resources.helpers.IPWhitelistResourceHelper;
import com.alexis.resources.requests.IPWhitelistRequest;
import com.alexis.stores.IPWhitelistDAO;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/ipwhitelists")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class IPWhitelistResource extends RestResource {

	private long environmentId;
	private long applicationId;

	public IPWhitelistResource() {

	}

	public IPWhitelistResource(long environmentId, long applicationId, ServletContext context) {
		super(context);
		this.environmentId = environmentId;
		this.applicationId = applicationId;
	}

	@POST
	public Response postIPToWhitelist(IPWhitelistRequest ipWhitelistRequest) {
		try {
			return IPWhitelistResourceHelper.whitelistIP(getConnection(), environmentId, applicationId, ipWhitelistRequest);
		} catch (SQLException e) {
			throw new WebApplicationException(e);
		}
	}

	@GET
	public Response getWhitelistIPs(@QueryParam("clientName") String clientName, @QueryParam("environmentId") long environmentId, @QueryParam("applicationId") long applicationId) {
		try {
			if (environmentId > 0) {
				this.environmentId = environmentId;
			}

			if (applicationId > 0) {
				this.applicationId = applicationId;
			}
			List<IPAddress> whitelistIPS = new IPWhitelistDAO(getConnection()).getWhitelist(this.environmentId, this.applicationId, clientName);

			GenericEntity<List<IPAddress>> entity = new GenericEntity<List<IPAddress>>(whitelistIPS) {};
			return Response.ok(entity).build();
		} catch (SQLException e) {
			throw new WebApplicationException(e);
		}
	}

	@DELETE
	@Path("{id : [0-9]+(,[0-9]+)*}")
	public Response deleteById(@PathParam("id") long ipAddressId, @QueryParam("clientName") String clientName, @QueryParam("environmentId") long environmentId, @QueryParam("applicationId") long applicationId) {
		return delete(ipAddressId, clientName, environmentId, applicationId);
	}

	@DELETE
	public Response delete(@QueryParam("ipAddressId") long ipAddressId, @QueryParam("clientName") String clientName, @QueryParam("environmentId") long environmentId, @QueryParam("applicationId") long applicationId) {
		try {
			if (environmentId > 0) {
				this.environmentId = environmentId;
			}

			if (applicationId > 0) {
				this.applicationId = applicationId;
			}

			IPWhitelistResourceHelper.deleteWhitelistIP(getConnection(), ipAddressId, this.environmentId, this.applicationId, clientName);

			return Response.noContent().build();
		} catch (SQLException e) {
			throw new WebApplicationException(e);
		}
	}
}