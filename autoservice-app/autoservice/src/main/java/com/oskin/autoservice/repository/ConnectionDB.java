package com.oskin.autoservice.repository;

import com.oskin.annotations.Inject;
import com.oskin.annotations.Singleton;
import com.oskin.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public final class ConnectionDB {
    @Inject
    Config config;
    private final Logger logger = LoggerFactory.getLogger(ConnectionDB.class);
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
        logger.info("Start connect to DB");
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(config.getUrlBd(), config.getUserBd(), config.getPasswordBd());
                connection.setAutoCommit(false);
                logger.info("successful connect to DB");
            } catch (java.sql.SQLException e) {
                logger.error("error connect {}", e.getMessage());
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
            logger.info("Start commit");
            getConnection().commit();
            logger.info("successful commit");
        } catch (SQLException e) {
            logger.error("error commit {}", e.getMessage());
        }
    }

    public void rollback() {
        try {
            logger.info("Start rollback");
            getConnection().rollback();
            logger.info("successful rollback");
        } catch (SQLException e) {
            logger.error("error rollback {}", e.getMessage());
        }
    }
}
