package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.Annotations.Singleton;
import com.oskin.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;

@Singleton
public class ConnectionDB {
    @Inject
    Config config;
    private static ConnectionDB instance;
    private Connection connection;
    private ConnectionDB() {};
    public static ConnectionDB GetInstance(){
        if(instance == null){
            instance = new ConnectionDB();
        }
        return instance;
    }

    public void Connect(){
        if(connection == null){
            try {
                connection = DriverManager.getConnection(config.getUrlBd(),config.getUserBd(),config.getPasswordBd());
                connection.setAutoCommit(false);
            } catch (java.sql.SQLException e){
                System.out.println("Произошла ошибка при подключении к базе данных");
            }
        }
    }

    public Connection getConnection(){
        if(connection == null){
            Connect();
        }
        return connection;
    }
}
