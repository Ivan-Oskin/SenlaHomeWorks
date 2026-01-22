package com.oskin.autoservice.repository;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Place;
import com.oskin.autoservice.Model.SortType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlaceRepository implements CrudRepository<Place> {
    @Inject
    ConnectionDB connectionDB;
    Scanner scanner = new Scanner(System.in);

    @Override
    public <G extends SortType> ArrayList<Place> findAll(G sortType) {
        ArrayList<Place> places = new ArrayList<>();
        String sql = "SELECT * FROM places ORDER BY " + sortType.getStringSortType();
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int id = set.getInt("id");
                String name = set.getString("name");
                places.add(new Place(id, name));
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return places;
    }

    @Override
    public void create(Place place) {
        String sql = "INSERT INTO places (id, name) VALUES (?, ?)";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, place.getId());
            statement.setString(2, place.getName());
            statement.executeUpdate();
            connectionDB.commit();
        } catch (java.sql.SQLException e) {
            connectionDB.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM places WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if (result > 0) {
                connectionDB.commit();
                return true;
            }
        } catch (java.sql.SQLException e) {
            connectionDB.rollback();
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String name) {
        Place place = find(name);
        return delete(place.getId());
    }

    @Override
    public Place find(int id) {
        String sql = "SELECT * FROM places WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                String name = set.getString("name");
                return new Place(id, name);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Place place) {
        String sql = "UPDATE places SET name = ? WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, place.getName());
            statement.setInt(2, place.getId());
            statement.executeUpdate();
            connectionDB.commit();
        } catch (SQLException e) {
            connectionDB.rollback();
            e.printStackTrace();
        }
    }

    public Place find(String name) {
        String sql = "SELECT * FROM places WHERE name = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            ArrayList<Place> places = new ArrayList<>();
            while (set.next()) {
                int id = set.getInt("id");
                places.add(new Place(id, name));
            }
            if (places.size() > 1) {
                while (true) {
                    System.out.println("Было найдено несколько записей. Выберите какую запись выбрать: ");
                    int count = 1;
                    for (Place place : places) {
                        System.out.println(count + ": id:" + place.getId() + " name: " + place.getName());
                        count++;
                    }
                    int x = 0;
                    try {
                        x = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                    }
                    if (x > 0 && x < places.size() + 1) {
                        return places.get(x - 1);
                    } else {
                        System.out.println("Неправильный ввод");
                    }
                }
            } else if (places.size() == 1) {
                return places.get(0);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
