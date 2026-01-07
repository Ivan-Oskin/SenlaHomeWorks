package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Controller.CarRepair;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.Place;
import com.oskin.autoservice.Model.StatusOrder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class OrderDB {
    @Inject
    ConnectionDB connectionDB;
    @Inject
    PlaceBD placeBD;
    @Inject
    CarRepair carRepair;
    @Inject
    FunctionsDB functionsDB;
    public ArrayList<Order> selectOrder(){
        ArrayList<Order> orders = new ArrayList<>();
        try(Statement statement = connectionDB.getConnection().createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM Orders");
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
                ArrayList<Place> places = placeBD.selectPlace();
                int num = carRepair.findById(place_id, places);
                if(num > -1){
                    orders.add(new Order(id, name, cost, places.get(num), createTime, startTime, completeTime, status));
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
        String sql = "SELECT * FROM Orders WHERE name = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
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
                ArrayList<Place> places = placeBD.selectPlace();
                int num = carRepair.findById(place_id, places);
                if(num > -1){
                    return new Order(id, name, cost, places.get(num), createTime, startTime, completeTime, status);
                }
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteOrderInDB(String name){
        return functionsDB.deleteInDB(name, NameTables.ORDER);
    }
    public boolean deleteOrderInDB(int id){
        return functionsDB.deleteInDB(id, NameTables.ORDER);
    }

    public void addOrderInDB(Order order){
        String sql = "INSERT INTO Orders (id, name, status, timeCreate, timeStart, timeComplete, cost, place_id) VALUES (?,?,?,?,?,?,?,?)";
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
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }
    public boolean ChangeStatusInDb(String name, StatusOrder statusOrder) {
        String sql = "UPDATE Orders SET status = ? WHERE name = ?;";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)){
            statement.setString(1, statusOrder.getSTATUS());
            statement.setString(2, name);
            int change = statement.executeUpdate();
            System.out.println(change);
            if(change > 0) return true;
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean offsetInDb(String name, LocalDateTime timeStart, LocalDateTime timeComplete) {
        String sql = "UPDATE Orders SET timeStart = ?, timeComplete = ? WHERE name = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)){
            statement.setTimestamp(1, Timestamp.valueOf(timeStart));
            statement.setTimestamp(2, Timestamp.valueOf(timeComplete));
            statement.setString(3, name);
            int inf = statement.executeUpdate();
            if(inf > 0) return true;
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
