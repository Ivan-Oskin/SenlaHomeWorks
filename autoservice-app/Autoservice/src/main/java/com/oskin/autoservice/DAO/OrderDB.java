package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.Place;
import com.oskin.autoservice.Model.SortTypeOrder;
import com.oskin.autoservice.Model.StatusOrder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OrderDB {
    @Inject
    ConnectionDB connectionDB;
    @Inject
    PlaceBD placeBD;
    @Inject
    FunctionsDB functionsDB;
    Scanner scanner = new Scanner(System.in);
    public ArrayList<Order> selectOrder(SortTypeOrder sortTypeOrder){
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, sortTypeOrder.getStringSortType());
            ResultSet set = statement.executeQuery();
            while (set.next()){
                int id = set.getInt("id");
                String name = set.getString("name");
                StatusOrder status = StatusOrder.ACTIVE;
                for(StatusOrder statusOrder : StatusOrder.values()){
                    if(set.getString("status").equals(statusOrder.getSTATUS())){
                        status = statusOrder;
                        break;
                    }
                }
                LocalDateTime createTime = set.getTimestamp("timeCreate").toLocalDateTime();
                LocalDateTime startTime = set.getTimestamp("timeStart").toLocalDateTime();
                LocalDateTime completeTime = set.getTimestamp("timeComplete").toLocalDateTime();
                int cost = set.getInt("cost");
                int place_id = set.getInt("place_id");
                Place place = placeBD.findPlaceInDb(place_id);
                if(place != null){
                    orders.add(new Order(id, name, cost, place, createTime, startTime, completeTime, status));
                }
                else{
                    System.out.println("Место с id "+place_id+" не найдено. Заказ "+name+" некоректен");
                }
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return orders;
    }
    public Order findOrderInDB(String name){
        String sql = "SELECT * FROM orders WHERE name = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            ArrayList<Order> orders = new ArrayList<>();
            while (set.next()){
                int id = set.getInt("id");
                StatusOrder status = StatusOrder.ACTIVE;
                for(StatusOrder statusOrder : StatusOrder.values()){
                    if(set.getString("status").equals(statusOrder.getSTATUS())){
                        status = statusOrder;
                        break;
                    }
                }
                LocalDateTime createTime = set.getTimestamp("timeCreate").toLocalDateTime();
                LocalDateTime startTime = set.getTimestamp("timeStart").toLocalDateTime();
                LocalDateTime completeTime = set.getTimestamp("timeComplete").toLocalDateTime();
                int cost = set.getInt("cost");
                int place_id = set.getInt("place_id");
                Place place = placeBD.findPlaceInDb(place_id);
                if(place != null){
                     orders.add(new Order(id, name, cost, place, createTime, startTime, completeTime, status));
                }
            }
            if(orders.size() > 1){
                while (true){
                    System.out.println("Было найдено несколько записей. Выберите какую запись выбрать: ");
                    int count = 1;
                    for(Order order : orders){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
                        System.out.println(count + ": id:" +  order.getId() + " name: "+order.getName() + " Create Time: "+order.getTimeCreate().format(formatter));
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
                    if(x > 0 && x < orders.size()+1){
                        return orders.get(x-1);
                    }
                    else {
                        System.out.println("Неправильный ввод");
                    }
                }
            }else if(orders.size() == 1) {
                return orders.get(0);
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public Order findOrderInDB(int idOrder){
        String sql = "SELECT * FROM orders WHERE id = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idOrder);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                String name = set.getString("name");
                StatusOrder status = StatusOrder.ACTIVE;
                for(StatusOrder statusOrder : StatusOrder.values()){
                    if(set.getString("status").equals(statusOrder.getSTATUS())){
                        status = statusOrder;
                        break;
                    }
                }
                LocalDateTime createTime = set.getTimestamp("timeCreate").toLocalDateTime();
                LocalDateTime startTime = set.getTimestamp("timeStart").toLocalDateTime();
                LocalDateTime completeTime = set.getTimestamp("timeComplete").toLocalDateTime();
                int cost = set.getInt("cost");
                int place_id = set.getInt("place_id");
                Place place = placeBD.findPlaceInDb(place_id);
                if(place != null){
                    return new Order(idOrder, name, cost, place, createTime, startTime, completeTime, status);
                }
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean deleteOrderInDB(String name){
        Order order = findOrderInDB(name);
        return functionsDB.deleteInDB(order.getId(), NameTables.ORDER);
    }
    public boolean deleteOrderInDB(int id){
        return functionsDB.deleteInDB(id, NameTables.ORDER);
    }

    public void addOrderInDB(Order order){
        String sql = "INSERT INTO orders (id, name, status, timeCreate, timeStart, timeComplete, cost, place_id) VALUES (?,?,?,?,?,?,?,?)";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, order.getId());
            statement.setString(2, order.getName());
            statement.setString(3, order.getStatus().getSTATUS());
            statement.setTimestamp(4,Timestamp.valueOf(order.getTimeCreate()));
            statement.setTimestamp(5,Timestamp.valueOf(order.getTimeStart()));
            statement.setTimestamp(6,Timestamp.valueOf(order.getTimeComplete()));
            statement.setInt(7, order.getCost());
            statement.setInt(8, order.getPlace().getId());
            statement.executeUpdate();
            functionsDB.commit();
        } catch (java.sql.SQLException e){
            functionsDB.rollback();
            e.printStackTrace();
        }
    }
    public boolean ChangeStatusInDb(String name, StatusOrder statusOrder) {
        String sql = "UPDATE orders SET status = ? WHERE name = ?;";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)){
            statement.setString(1, statusOrder.getSTATUS());
            statement.setString(2, name);
            int change = statement.executeUpdate();
            System.out.println(change);
            if(change > 0) {
                functionsDB.commit();
                return true;
            }
        } catch (java.sql.SQLException e){
            functionsDB.rollback();
            e.printStackTrace();
        }
        return false;
    }
    public boolean offsetInDb(String name, LocalDateTime timeStart, LocalDateTime timeComplete) {
        String sql = "UPDATE orders SET timeStart = ?, timeComplete = ? WHERE name = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)){
            statement.setTimestamp(1, Timestamp.valueOf(timeStart));
            statement.setTimestamp(2, Timestamp.valueOf(timeComplete));
            statement.setString(3, name);
            int inf = statement.executeUpdate();
            System.out.println(inf);
            if(inf > 0) {
                functionsDB.commit();
                return true;
            }
        } catch (java.sql.SQLException e){
            functionsDB.rollback();
            e.printStackTrace();
        }
        return false;
    }
}
