package com.alexis.stores;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Store<T> {

    protected Connection connection;

    public Store(Connection connection) {
        super();
        this.connection = connection;
    }

    protected abstract T build(ResultSet resultSet) throws SQLException;

}
