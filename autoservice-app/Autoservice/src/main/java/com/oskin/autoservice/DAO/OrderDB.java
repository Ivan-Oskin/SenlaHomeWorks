package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Controller.CarRepair;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.Place;
import com.oskin.autoservice.Model.StatusOrder;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderDB {
    @Inject
    ConnectionDB connectionDB;
    @Inject
    PlaceBD placeBD;
    @Inject
    CarRepair carRepair;
    public ArrayList<Order> selectOrder(){
        ArrayList<Order> orders = new ArrayList<>();
        try(Statement statement = connectionDB.getConnection().createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM Orders");
            while (set.next()){
                int id = set.getInt("id");
                String name = set.getString("name");
                StatusOrder status = StatusOrder.ACTIVE;
                for(StatusOrder statusOrder : StatusOrder.values()){
                    if(statusOrder.getSTATUS().equals(set.getString("status"))){
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
}
