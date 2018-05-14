package com.alexis.stores;

import com.alexis.domains.Environment;
import com.alexis.exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentDAO extends Store<Environment> {

    public EnvironmentDAO(Connection connection) {
        super(connection);
    }

    public List<Environment> getEnvironmentList() throws SQLException {
        List<Environment> environments = new ArrayList<Environment>();
        String query = "SELECT * FROM SERVERENVIRONMENTS";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs;
        try {
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                environments.add(build(rs));
            }
        } finally {
            preparedStatement.close();
        }

        return environments;
    }

    public Environment getEnvironment(long environmentId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM SERVERENVIRONMENTS WHERE ID=?");
        preparedStatement.setLong(1, environmentId);
        Environment environment = null;
        ResultSet rs;
        try {
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                environment = build(rs);
            }

            return environment;
        } finally {
            preparedStatement.close();
        }

    }

    @Override
    protected Environment build(ResultSet resultSet) throws SQLException {
        Environment environment = new Environment();
        environment.setId(resultSet.getLong("id"));
        environment.setName(resultSet.getString("name"));

        return  environment;
    }
}
