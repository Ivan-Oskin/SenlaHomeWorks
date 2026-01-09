package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Master;
import com.oskin.autoservice.Model.Place;
import com.oskin.autoservice.View.CarRepairInput;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlaceBD {
    @Inject
    ConnectionDB connectionDB;
    @Inject
    FunctionsDB functionsDB;
    Scanner scanner = new Scanner(System.in);
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
            functionsDB.commit();
        } catch (java.sql.SQLException e){
            functionsDB.rollback();
            e.printStackTrace();
        }
    }
    public boolean deletePlaceInDB(String name){
        Place place = findPlaceInDb(name);
        return functionsDB.deleteInDB(place.getId(), NameTables.PLACE);
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
            ArrayList<Place> places = new ArrayList<>();
            while (set.next()){
                int id = set.getInt("id");
                places.add(new Place(id, name));
            }
            if(places.size() > 1){
                while (true){
                    System.out.println("Было найдено несколько записей. Выберите какую запись выбрать: ");
                    int count = 1;
                    for(Place place : places){
                        System.out.println(count + ": id:" +  place.getId() + " name: "+place.getName());
                        count++;
                    }
                    int x = 0;
                    try{
                        x = scanner.nextInt();
                        scanner.nextLine();
                    }
                    catch (InputMismatchException e){
                        scanner.nextLine();
                    }
                    if(x > 0 && x < places.size()+1){
                        return places.get(x-1);
                    }
                    else {
                        System.out.println("Неправильный ввод");
                    }
                }
            } else if(places.size() == 1) {
                return places.get(0);
                }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
