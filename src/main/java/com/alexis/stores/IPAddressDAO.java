package com.alexis.stores;

import com.alexis.domains.Client;
import com.alexis.domains.IPAddress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IPAddressDAO extends Store<IPAddress> {

    public IPAddressDAO(Connection connection) {
        super(connection);
    }

    public IPAddress getIpAddress(String ip) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM IPADDRESSES WHERE ip=?");
        preparedStatement.setString(1, ip);
        IPAddress ipAddress = null;
        ResultSet rs;
        try {
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ipAddress = build(rs);
            }

            return ipAddress;
        } finally {
            preparedStatement.close();
        }
    }

    public boolean clientIpExist(long ipAddressId, long clientId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CLIENTSIPS WHERE ipaddressid=? and clientid=?");
        preparedStatement.setLong(1, ipAddressId);
        preparedStatement.setLong(2, clientId);

        try {
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } finally {
            preparedStatement.close();
        }
    }

    public void insertClientIP(long ipAddressId, long clientId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CLIENTSIPS(IPADDRESSID, CLIENTID) VALUES(?, ?)");
        preparedStatement.setLong(1, ipAddressId);
        preparedStatement.setLong(2, clientId);

        try {
            preparedStatement.executeUpdate();
        } finally {
            preparedStatement.close();
        }
    }

    public IPAddress insertIPAddress(IPAddress ipAddress) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO IPADDRESSES (ip) VALUES(?)");
        preparedStatement.setString(1, ipAddress.getIp());

        try {
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("CALL IDENTITY()");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ipAddress.setId(resultSet.getInt(1));
            }

            return ipAddress;
        } finally {
            preparedStatement.close();
        }
    }

    @Override
    protected IPAddress build(ResultSet resultSet) throws SQLException {
        IPAddress ipAddress = new IPAddress();
        ipAddress.setId(resultSet.getLong("id"));
        ipAddress.setIp(resultSet.getString("ip"));

        return ipAddress;
    }
}
