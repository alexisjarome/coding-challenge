package com.alexis.resources;

import com.alexis.listeners.DBContextListener;

import javax.servlet.ServletContext;
import java.sql.Connection;

public class RestResource {

    @javax.ws.rs.core.Context
    protected ServletContext context;

    public RestResource() {

    }

    public RestResource(ServletContext context) {
        this.context = context;
    }

    protected Connection getConnection() {
        Connection connection = (Connection) context.getAttribute(DBContextListener.DB_CONNECTION_ATTRIBUTE);
        return connection;
    }
}
