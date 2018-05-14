package com.alexis.listeners;

import com.alexis.db.DBServer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class DBContextListener implements ServletContextListener {

    public static final String DB_CONNECTION_ATTRIBUTE = "DBConnection";
    private DBServer hsqlDBServer;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if (hsqlDBServer ==  null) {
            hsqlDBServer = new DBServer();
            try {
                hsqlDBServer.initializeDB();
                ServletContext servletContext = servletContextEvent.getServletContext();
                servletContext.setAttribute(DB_CONNECTION_ATTRIBUTE, hsqlDBServer.getConnection());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (hsqlDBServer != null) {
            try {
                hsqlDBServer.shutDownDBServer();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
