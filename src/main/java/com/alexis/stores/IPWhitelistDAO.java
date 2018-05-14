package com.alexis.stores;

import com.alexis.domains.Environment;
import com.alexis.domains.IPAddress;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IPWhitelistDAO {

    private Connection connection;

    public IPWhitelistDAO() {

    }

    public IPWhitelistDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean existWhitelist(long environmentId, long applicationId, String ip, String clientName) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM IPWHITELISTS iwl INNER JOIN IPADDRESSES ip on iwl.IPADDRESSID = ip.id INNER JOIN CLIENTS cl ON iwl.clientid = cl.id WHERE iwl.ENVIRONMENTID=? AND iwl.APPLICATIONID=? AND ip.ip=? AND cl.name=?");
        preparedStatement.setLong(1, environmentId);
        preparedStatement.setLong(2, applicationId);
        preparedStatement.setString(3, ip);
        preparedStatement.setString(4, clientName);
        try {
        ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } finally {
            preparedStatement.close();
        }
    }

    public void insertIPWhitelist(long environmentId, long applicationId, long ipAddressID, long clientId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO IPWHITELISTS VALUES(?, ?, ?, ?)");
        preparedStatement.setLong(1, environmentId);
        preparedStatement.setLong(2, applicationId);
        preparedStatement.setLong(3, ipAddressID);
        preparedStatement.setLong(4, clientId);

        try {
            preparedStatement.executeUpdate();
        } finally {
            preparedStatement.close();
        }
    }

    public List<IPAddress> getWhitelist(long environmentId, long applicationId, String clientName) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM IPADDRESSES ip ");
        boolean hasWhere = false;
        if (environmentId > 0 || applicationId > 0 || StringUtils.isNotBlank(clientName)) {
            sql.append(" WHERE ip.id IN (");
            sql.append("SELECT DISTINCT(IPADDRESSID) FROM IPWHITELISTS WHERE ");

            if (environmentId > 0) {
                sql.append("ENVIRONMENTID=? ");
                hasWhere = true;
            }

            if (applicationId > 0) {
                if (hasWhere) {
                    sql.append("AND ");
                }
                sql.append("APPLICATIONID=? ");
                hasWhere = true;
            }

            if (StringUtils.isNotBlank(clientName)) {
                if (hasWhere) {
                    sql.append("AND ");
                }
                sql.append("CLIENTID IN (SELECT ID FROM CLIENTS WHERE NAME=?)");
            }

            sql.append(")");
        }

        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        int index = 1;
        if (environmentId > 0) {
            preparedStatement.setLong(index++, environmentId);
        }

        if (applicationId > 0) {
            preparedStatement.setLong(index++, applicationId);
        }

        if (StringUtils.isNotBlank(clientName)) {
            preparedStatement.setString(index, clientName);
        }

        List<IPAddress> ipAddresses = new ArrayList<IPAddress>();
        try {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                IPAddress ipAddress = new IPAddress();
                ipAddress.setId(rs.getLong("id"));
                ipAddress.setIp(rs.getString("ip"));
                ipAddresses.add(ipAddress);
            }
        } finally {
            preparedStatement.close();
        }

        return ipAddresses;
    }

    public void deleteWhitelistIP(long ipAddressId, long environmentId, long applicationId, String clientName) throws SQLException {
        String sql = new String("DELETE FROM IPWHITELISTS WHERE IPADDRESSID=? AND ENVIRONMENTID=? AND APPLICATIONID=? AND CLIENTID=(SELECT ID FROM CLIENTS WHERE NAME=?)");
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, ipAddressId);
        preparedStatement.setLong(2, environmentId);
        preparedStatement.setLong(3, applicationId);
        preparedStatement.setString(4, clientName);

        try {
           int result =  preparedStatement.executeUpdate();
           System.out.println(result);
        } finally {
            preparedStatement.close();
        }
    }
}
