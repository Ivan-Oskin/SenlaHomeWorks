package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Singleton;

import java.sql.Connection;
import java.sql.DriverManager;

@Singleton
public class ConnectionDB {
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
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/carrepairdb", "carrepair_admin", "Admin");
            } catch (java.sql.SQLException e){
                System.out.println("Произошла ошибка при подключении к базе данных");
            }
        }
    }
}
