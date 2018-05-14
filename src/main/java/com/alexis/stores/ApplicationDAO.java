package com.alexis.stores;

import com.alexis.domains.Application;
import com.alexis.domains.Environment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO extends Store<Application> {


    public ApplicationDAO(Connection connection) {
        super(connection);
    }

    public List<Application> getApplicationsByEnvironment(long environmentId) throws SQLException {
        List<Application> applications = new ArrayList<Application>();
        String query = "SELECT * FROM APPLICATIONS app INNER JOIN APPLICATIONSENVIRONMENTS appenv on app.id = appenv.applicationId where appenv.environmentId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, environmentId);
        ResultSet rs;
        try {
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                applications.add(build(rs));
            }
        } finally {
            preparedStatement.close();
        }

        return applications;
    }
    public List<Application> getApplicationList() throws SQLException {
        List<Application> applications = new ArrayList<Application>();
        String query = "SELECT * FROM APPLICATIONS";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs;
        try {
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                applications.add(build(rs));
            }
        } finally {
            preparedStatement.close();
        }

        return applications;
    }

    public Application getApplication(long applicationId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM APPLICATIONS WHERE ID=?");
        preparedStatement.setLong(1, applicationId);
        Application application = null;
        ResultSet rs;
        try {
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                application = build(rs);
            }

            return application;
        } finally {
            preparedStatement.close();
        }

    }

    @Override
    protected Application build(ResultSet resultSet) throws SQLException {
        Application application = new Application();
        application.setId(resultSet.getLong("id"));
        application.setName(resultSet.getString("name"));

        return application;
    }
}
