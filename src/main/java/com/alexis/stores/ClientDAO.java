package com.alexis.stores;

import com.alexis.domains.Client;
import com.alexis.domains.Environment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO extends Store<Client> {

    public ClientDAO(Connection connection) {
        super(connection);
    }

    public List<Client> getClients() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CLIENTS");

        List<Client> clients = new ArrayList<Client>();
        try {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                clients.add(build(rs));
            }
        } finally {
            preparedStatement.close();
        }

        return clients;
    }

    public Client getClientByName(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CLIENTS WHERE NAME=?");
        preparedStatement.setString(1, name);
        Client client = null;
        try {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                client = build(rs);
            }

            return client;
        } finally {
            preparedStatement.close();
        }
    }

    public Client getClient(long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CLIENTS WHERE ID=?");
        preparedStatement.setLong(1, id);
        Client client = null;
        try {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                client = build(rs);
            }

            return client;
        } finally {
            preparedStatement.close();
        }
    }

    public Client insertClient(Client client) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CLIENTS (name) VALUES(?)");
        preparedStatement.setString(1, client.getName());

        try {
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("CALL IDENTITY()");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                client.setId(resultSet.getInt(1));
            }

            return client;
        } finally {
            preparedStatement.close();
        }
    }

    @Override
    protected Client build(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setId(resultSet.getLong("id"));
        client.setName(resultSet.getString("name"));
        return client;
    }
}
