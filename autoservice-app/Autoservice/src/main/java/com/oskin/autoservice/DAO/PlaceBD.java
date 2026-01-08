package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Place;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PlaceBD {
    @Inject
    ConnectionDB connectionDB;
    @Inject
    FunctionsDB functionsDB;
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
        return functionsDB.deleteInDB(name, NameTables.PLACE);
    }
    public boolean deletePlaceInDB(int id){
        return functionsDB.deleteInDB(id, NameTables.PLACE);
    }
    public Place findPlaceInDb(int id){
        String sql = "SELECT * FROM Places WHERE id = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                String name = set.getString("name");
                return new Place(id, name);
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public Place findPlaceInDb(String name){
        String sql = "SELECT * FROM Places WHERE name = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                int id = set.getInt("id");
                return new Place(id, name);
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
