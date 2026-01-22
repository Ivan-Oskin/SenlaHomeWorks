package com.oskin.autoservice.repository;

import com.oskin.Annotations.Inject;
import com.oskin.Annotations.Singleton;
import com.oskin.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public final class ConnectionDB {
    @Inject
    Config config;
    private static ConnectionDB instance;
    private Connection connection;

    private ConnectionDB() {
    }

    public static ConnectionDB getInstance() {
        if (instance == null) {
            instance = new ConnectionDB();
        }
        return instance;
    }

    public void connect() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(config.getUrlBd(), config.getUserBd(), config.getPasswordBd());
                connection.setAutoCommit(false);
            } catch (java.sql.SQLException e) {
                System.out.println("Произошла ошибка при подключении к базе данных");
            }
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            connect();
        }
        return connection;
    }

    public void commit() {
        try {
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollback() {
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
