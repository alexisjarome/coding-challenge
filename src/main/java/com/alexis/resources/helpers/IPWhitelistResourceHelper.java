package com.alexis.resources.helpers;

import com.alexis.domains.Application;
import com.alexis.domains.Client;
import com.alexis.domains.Environment;
import com.alexis.domains.IPAddress;
import com.alexis.exceptions.BadRequestException;
import com.alexis.exceptions.NotFoundException;
import com.alexis.resources.requests.IPWhitelistRequest;
import com.alexis.resources.response.SuccessPostResponse;
import com.alexis.stores.*;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

public class IPWhitelistResourceHelper {

    public static Response whitelistIP(Connection connection, long environmentId, long applicationId, IPWhitelistRequest whitelistIPRequest) throws SQLException {
        validateRequest(environmentId, applicationId, whitelistIPRequest);
        ClientDAO clientDAO = new ClientDAO(connection);
        IPWhitelistDAO ipWhitelistStore = new IPWhitelistDAO(connection);
        IPAddressDAO ipStore = new IPAddressDAO(connection);
        EnvironmentDAO environmentStore = new EnvironmentDAO(connection);
        ApplicationDAO applicationDAO = new ApplicationDAO(connection);

        Environment environment = environmentStore.getEnvironment(environmentId);

        if (environment == null) {
            throw new NotFoundException("Environment: " + environmentId + " not found");
        }

        Application application = applicationDAO.getApplication(applicationId);

        if (application == null) {
            throw new NotFoundException("Application: " + applicationId + " not found");
        }

        boolean exist = ipWhitelistStore.existWhitelist(environmentId, applicationId, whitelistIPRequest.getIpAddress(), whitelistIPRequest.getClientName());

        if (exist) {
            throw new BadRequestException("Client IP already whitelisted in the given client, environment and application");
        } else {
            Client client = clientDAO.getClientByName(whitelistIPRequest.getClientName());
            IPAddress ipAddress = ipStore.getIpAddress(whitelistIPRequest.getIpAddress());

            if (client == null) {
                client = new Client();
                client.setName(whitelistIPRequest.getClientName());
                client = clientDAO.insertClient(client);
            }

            if (ipAddress == null) {
                ipAddress = new IPAddress();
                ipAddress.setIp(whitelistIPRequest.getIpAddress());
                ipAddress = ipStore.insertIPAddress(ipAddress);
            }

            if (!ipStore.clientIpExist(ipAddress.getId(), client.getId())) {
                ipStore.insertClientIP(ipAddress.getId(), client.getId());
            }

            ipWhitelistStore.insertIPWhitelist(environmentId, applicationId, ipAddress.getId(), client.getId());
            String message = MessageFormat.format("Inserting to Whitelist successful for IP: {0}. Client: {1}, App: {2}, Environment: {3}", ipAddress.getIp(), client.getName(), application.getName(), environment.getName());
            return Response.ok(new SuccessPostResponse(message)).build();
        }

    }

    public static void deleteWhitelistIP(Connection connection, long ipAddressId, long environmentId, long applicationId, String clientName) throws SQLException {
        if (ipAddressId == 0) {
            throw new BadRequestException("IP Address id is not given");
        }
        validateEnvironmentAndApplication(environmentId, applicationId);
        validateClientName(clientName);

        new IPWhitelistDAO(connection).deleteWhitelistIP(ipAddressId, environmentId, applicationId, clientName);
    }

    private static void validateRequest(long environmentId, long applicationId, IPWhitelistRequest whitelistIPRequest) {
        validateEnvironmentAndApplication(environmentId, applicationId);
        validateClientName(whitelistIPRequest.getClientName());

        if (StringUtils.isBlank(whitelistIPRequest.getIpAddress())) {
            throw new BadRequestException("IP Address is not given");
        }

        try {
            InetAddress address = InetAddress.getByName(whitelistIPRequest.getIpAddress());
            if (address instanceof Inet6Address) {
                throw new BadRequestException("IP is not IPv4");
            } else {
                whitelistIPRequest.setIpAddress(address.getHostAddress());
            }
        } catch (UnknownHostException e) {
            throw new BadRequestException("IP is not IPv4");
        }
    }

    private static void validateEnvironmentAndApplication(long environmentId, long applicationId) {
        if (environmentId == 0) {
            throw new BadRequestException("Environment ID not given");
        }

        if (applicationId == 0) {
            throw new BadRequestException("Application ID not given");
        }
    }

    private static void validateClientName(String clientName) {
        if (StringUtils.isBlank(clientName)) {
            throw new BadRequestException("Client Name is not given");
        }
    }
}
