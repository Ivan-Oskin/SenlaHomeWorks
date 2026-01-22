package com.oskin.autoservice.repository;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Place;
import com.oskin.autoservice.Model.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlaceRepository implements CrudRepository<Place> {
    @Inject
    ConnectionDB connectionDB;
    private final Logger logger = LoggerFactory.getLogger(PlaceRepository.class);
    Scanner scanner = new Scanner(System.in);

    @Override
    public <G extends SortType> ArrayList<Place> findAll(G sortType) {
        logger.info("Start findAll place ");
        ArrayList<Place> places = new ArrayList<>();
        String sql = "SELECT * FROM places ORDER BY " + sortType.getStringSortType();
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int id = set.getInt("id");
                String name = set.getString("name");
                places.add(new Place(id, name));
            }
            logger.info("successful findAll place ");
        } catch (java.sql.SQLException e) {
            logger.error("error findAll place {}", e.getMessage());
        }
        return places;
    }

    @Override
    public void create(Place place) {
        logger.info("Start create place ");
        String sql = "INSERT INTO places (id, name) VALUES (?, ?)";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, place.getId());
            statement.setString(2, place.getName());
            statement.executeUpdate();
            connectionDB.commit();
            logger.info("successful create place ");
        } catch (java.sql.SQLException e) {
            logger.error("error create place {}", e.getMessage());
            connectionDB.rollback();
        }
    }

    @Override
    public boolean delete(int id) {
        logger.info("Start delete place ");
        String sql = "DELETE FROM places WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if (result > 0) {
                connectionDB.commit();
                logger.info("successful delete place ");
                return true;
            }
        } catch (java.sql.SQLException e) {
            logger.error("error delete place {}", e.getMessage());
            connectionDB.rollback();
        }
        return false;
    }

    public boolean delete(String name) {
        Place place = find(name);
        return delete(place.getId());
    }

    @Override
    public Place find(int id) {
        logger.info("Start findById place ");
        String sql = "SELECT * FROM places WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                String name = set.getString("name");
                logger.info("successful findById place ");
                return new Place(id, name);
            }
        } catch (java.sql.SQLException e) {
            logger.error("error findById {}", e.getMessage());
        }
        logger.info("No found but successful findById place");
        return null;
    }

    @Override
    public void update(Place place) {
        logger.info("Start update place ");
        String sql = "UPDATE places SET name = ? WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, place.getName());
            statement.setInt(2, place.getId());
            statement.executeUpdate();
            connectionDB.commit();
            logger.info("successful update place ");
        } catch (SQLException e) {
            logger.error("error update place {}", e.getMessage());
            connectionDB.rollback();
        }
    }

    public Place find(String name) {
        logger.info("Start findByName place ");
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
                        logger.info("successful findByName with choice place ");
                        return places.get(x - 1);
                    } else {
                        System.out.println("Неправильный ввод");
                    }
                }
            } else if (places.size() == 1) {
                logger.info("successful findByName without choice place");
                return places.get(0);
            }
        } catch (java.sql.SQLException e) {
            logger.error("error findByName place {}", e.getMessage());
        }
        logger.info("No found but successful findByName place");
        return null;
    }
}
