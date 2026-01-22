package com.oskin.autoservice.repository;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.StatusOrder;
import com.oskin.autoservice.Model.Place;
import com.oskin.autoservice.Model.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OrderRepository implements CrudRepository<Order> {
    @Inject
    ConnectionDB connectionDB;
    @Inject
    PlaceRepository placeRepository;
    Scanner scanner = new Scanner(System.in);
    private final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Override
    public <G extends SortType> ArrayList<Order> findAll(G sortTypeOrder) {
        logger.info("start findAll order");
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY " + sortTypeOrder.getStringSortType();
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int id = set.getInt("id");
                String name = set.getString("name");
                StatusOrder status = StatusOrder.ACTIVE;
                for (StatusOrder statusOrder : StatusOrder.values()) {
                    if (set.getString("status").equals(statusOrder.getSTATUS())) {
                        status = statusOrder;
                        break;
                    }
                }
                LocalDateTime createTime = set.getTimestamp("timeCreate").toLocalDateTime();
                LocalDateTime startTime = set.getTimestamp("timeStart").toLocalDateTime();
                LocalDateTime completeTime = set.getTimestamp("timeComplete").toLocalDateTime();
                int cost = set.getInt("cost");
                int place_id = set.getInt("place_id");
                Place place = placeRepository.find(place_id);
                if (place != null) {
                    orders.add(new Order(id, name, cost, place, createTime, startTime, completeTime, status));
                } else {
                    System.out.println("Место с id " + place_id + " не найдено. Заказ " + name + " некоректен");
                }
            }
            logger.info("successful findAll order");
        } catch (java.sql.SQLException e) {
            logger.error("error findAll order {}", e.getMessage());
        }
        return orders;
    }

    public Order find(String name) {
        logger.info("start findByName order");
        String sql = "SELECT * FROM orders WHERE name = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            ArrayList<Order> orders = new ArrayList<>();
            while (set.next()) {
                int id = set.getInt("id");
                StatusOrder status = StatusOrder.ACTIVE;
                for (StatusOrder statusOrder : StatusOrder.values()) {
                    if (set.getString("status").equals(statusOrder.getSTATUS())) {
                        status = statusOrder;
                        break;
                    }
                }
                LocalDateTime createTime = set.getTimestamp("timeCreate").toLocalDateTime();
                LocalDateTime startTime = set.getTimestamp("timeStart").toLocalDateTime();
                LocalDateTime completeTime = set.getTimestamp("timeComplete").toLocalDateTime();
                int cost = set.getInt("cost");
                int place_id = set.getInt("place_id");
                Place place = placeRepository.find(place_id);
                if (place != null) {
                    orders.add(new Order(id, name, cost, place, createTime, startTime, completeTime, status));
                }
            }
            if (orders.size() > 1) {
                while (true) {
                    System.out.println("Было найдено несколько записей. Выберите какую запись выбрать: ");
                    int count = 1;
                    for (Order order : orders) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
                        System.out.println(count + ": id:" + order.getId() + " name: " + order.getName() + " Create Time: " + order.getTimeCreate().format(formatter));
                        count++;
                    }
                    int x = 0;
                    try {
                        x = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                    }
                    if (x > 0 && x < orders.size() + 1) {
                        logger.info("successful findByName with choice order");
                        return orders.get(x - 1);
                    } else {
                        System.out.println("Неправильный ввод");
                    }
                }
            } else if (orders.size() == 1) {
                logger.info("successful findByName without choice order");
                return orders.get(0);
            }
        } catch (java.sql.SQLException e) {
            logger.error("error findByName order {}", e.getMessage());
        }
        logger.info("No found but successful findByName order");
        return null;
    }

    @Override
    public Order find(int idOrder) {
        logger.info("start findById order");
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idOrder);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                String name = set.getString("name");
                StatusOrder status = StatusOrder.ACTIVE;
                for (StatusOrder statusOrder : StatusOrder.values()) {
                    if (set.getString("status").equals(statusOrder.getSTATUS())) {
                        status = statusOrder;
                        break;
                    }
                }
                LocalDateTime createTime = set.getTimestamp("timeCreate").toLocalDateTime();
                LocalDateTime startTime = set.getTimestamp("timeStart").toLocalDateTime();
                LocalDateTime completeTime = set.getTimestamp("timeComplete").toLocalDateTime();
                int cost = set.getInt("cost");
                int place_id = set.getInt("place_id");
                Place place = placeRepository.find(place_id);
                if (place != null) {
                    logger.info("successful findById order");
                    return new Order(idOrder, name, cost, place, createTime, startTime, completeTime, status);
                }
            }
        } catch (java.sql.SQLException e) {
            logger.error("error findById order {}", e.getMessage());
        }
        logger.info("No found but successful findById order");
        return null;
    }

    public boolean delete(String name) {
        Order order = find(name);
        if (order == null) {
            return false;
        } else {
            return delete(order.getId());
        }
    }

    @Override
    public boolean delete(int id) {
        logger.info("start delete order");
        String sql = "DELETE FROM orders WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if (result > 0) {
                connectionDB.commit();
                logger.info("successful delete order");
                return true;
            }
        } catch (java.sql.SQLException e) {
            connectionDB.rollback();
            logger.error("error delete order {}", e.getMessage());
        }
        return false;
    }

    @Override
    public void create(Order order) {
        logger.info("start create order");
        String sql = "INSERT INTO orders (id, name, status, timeCreate, timeStart, timeComplete, cost, place_id) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, order.getId());
            statement.setString(2, order.getName());
            statement.setString(3, order.getStatus().getSTATUS());
            statement.setTimestamp(4, Timestamp.valueOf(order.getTimeCreate()));
            statement.setTimestamp(5, Timestamp.valueOf(order.getTimeStart()));
            statement.setTimestamp(6, Timestamp.valueOf(order.getTimeComplete()));
            statement.setInt(7, order.getCost());
            statement.setInt(8, order.getPlace().getId());
            statement.executeUpdate();
            logger.info("successful create order");
            connectionDB.commit();
        } catch (java.sql.SQLException e) {
            logger.error("error create order {}", e.getMessage());
            connectionDB.rollback();
        }
    }

    public boolean changeStatusInDb(String name, StatusOrder statusOrder) {
        logger.info("start changeStatus order");
        String sql = "UPDATE orders SET status = ? WHERE name = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, statusOrder.getSTATUS());
            statement.setString(2, name);
            int change = statement.executeUpdate();
            System.out.println(change);
            if (change > 0) {
                logger.info("successful changeStatus order");
                connectionDB.commit();
                return true;
            }
        } catch (java.sql.SQLException e) {
            logger.error("error changeStatus order {}", e.getMessage());
            connectionDB.rollback();
        }
        return false;
    }

    public boolean offsetInDb(String name, LocalDateTime timeStart, LocalDateTime timeComplete) {
        logger.info("start offset order");
        String sql = "UPDATE orders SET timeStart = ?, timeComplete = ? WHERE name = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(timeStart));
            statement.setTimestamp(2, Timestamp.valueOf(timeComplete));
            statement.setString(3, name);
            int inf = statement.executeUpdate();
            System.out.println(inf);
            if (inf > 0) {
                logger.info("successful offset order");
                connectionDB.commit();
                return true;
            }
        } catch (java.sql.SQLException e) {
            logger.error("error offset order {}", e.getMessage());
            connectionDB.rollback();
        }
        return false;
    }

    public void update(Order order) {
        logger.info("start update order");
        String sql = "UPDATE orders SET name = ?, status = ?, timeCreate = ?, timeStart, timeComplete = ?, cost = ?, place_id = ? WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, order.getName());
            statement.setString(2, order.getStatus().getSTATUS());
            statement.setTimestamp(3, Timestamp.valueOf(order.getTimeCreate()));
            statement.setTimestamp(4, Timestamp.valueOf(order.getTimeStart()));
            statement.setTimestamp(5, Timestamp.valueOf(order.getTimeComplete()));
            statement.setInt(6, order.getCost());
            statement.setInt(7, order.getPlace().getId());
            statement.executeUpdate();
            logger.info("successful update order");
            connectionDB.commit();
        } catch (SQLException e) {
            logger.error("error update order {}", e.getMessage());
            connectionDB.rollback();
        }
    }
}
