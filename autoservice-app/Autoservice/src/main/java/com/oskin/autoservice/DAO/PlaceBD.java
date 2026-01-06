package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Place;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class PlaceBD {
    @Inject
    ConnectionDB connectionDB;
    public ArrayList<Place> selectPlace(){
        ArrayList<Place> places = new ArrayList<>();
        try(Statement statement = connectionDB.getConnection().createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM Places");
            while (set.next()){
                int id = set.getInt("id");
                String name = set.getString("name");
                places.add(new Place(id, name));
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return places;
    }
    public void addPlaceInDB(Place place){
        String sql = "INSERT INTO Places (id, name) VALUES (?, ?)";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, place.getId());
            statement.setString(2, place.getName());
            statement.executeUpdate();
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }
    public boolean deletePlaceInDB(String name){
        String sql = "DELETE FROM Places WHERE name = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            int result = statement.executeUpdate();
            if(result > 0){
                return true;
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean deletePlaceInDB(int id){
        String sql = "DELETE FROM Places WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if(result > 0){
                return true;
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean DeleteAll(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Вы уверены, это действие приведет к удалению ВСЕХ данных.\nY/N");
            String line =  scanner.nextLine();
            if(line.toLowerCase().equals("y")){
                try(Statement statement = connectionDB.getConnection().createStatement()) {
                    statement.executeUpdate("DELETE FROM Places");
                    return true;
                } catch (java.sql.SQLException e){
                    e.printStackTrace();
                }
                return false;
            }
            else if(line.toLowerCase().equals("n")){
                return false;
            }
        }

    }

}
