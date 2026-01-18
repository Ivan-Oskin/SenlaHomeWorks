package com.oskin.autoservice.Controller;
import com.oskin.Annotations.*;
import com.oskin.autoservice.DAO.MasterDB;
import com.oskin.autoservice.DAO.OrderDB;
import com.oskin.autoservice.DAO.OrdersByMasterDb;
import com.oskin.autoservice.Model.*;
import com.oskin.config.Config;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

@Singleton
public class CarRepairOrders {
    @Inject
    OrderDB orderDB;
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;
    @Inject
    MasterDB masterDB;
    @Inject
    OrdersByMasterDb ordersByMasterDb;
    @Inject
    CarRepairOrderMaster carRepairOrderMaster;

    private static CarRepairOrders instance;

    private CarRepairOrders(){}

    public static CarRepairOrders getInstance(){
        if(instance == null){
            instance = new CarRepairOrders();
        }
        return instance;
    }
    public void addOrder(int id, String name, int cost, Place place, LocalDateTime timeCreate, LocalDateTime timeStart, LocalDateTime timeCopmlete) {
        Order order = new Order(id, name, cost, place, timeCreate, timeStart, timeCopmlete);
        orderDB.addOrderInDB(order);
    }
    public boolean deleteOrder(String name) {
        return orderDB.deleteOrderInDB(name);
    }
    public boolean completeOrder(String name) {
        return orderDB.ChangeStatusInDb(name, StatusOrder.CLOSE);
    }
    public boolean cancelOrder(String name) {
        return orderDB.ChangeStatusInDb(name, StatusOrder.CANCEL);
    }
    public boolean offset(String name, int countDay, int countHour) {
        Order order = orderDB.findOrderInDB(name);
        if(order == null){
            System.out.println("не находит");
            return false;
        }
        else {
            LocalDateTime startTime = order.getTimeStart();
            LocalDateTime completeTime = order.getTimeComplete();
            LocalDateTime ChangeStartTime = startTime.plusDays(countDay);
            LocalDateTime ChangeCompleteTime = completeTime.plusDays(countDay);
            startTime = ChangeStartTime.plusHours(countHour);
            completeTime = ChangeCompleteTime.plusHours(countHour);
            return orderDB.offsetInDb(name, startTime,completeTime);
        }
    }

    public ArrayList<Order> getListOfOrders(SortTypeOrder sortType) {
        ArrayList<Order> newList = orderDB.selectOrder(sortType);
        return newList;
    }

    public ArrayList<Order> getListOfActiveOrders(SortTypeOrder sortType) {
        ArrayList<Order> orders = orderDB.selectOrder(sortType);
        ArrayList<Order> newList = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus().equals(StatusOrder.ACTIVE)) {
                newList.add(order);
            }
        }
        return newList;
    }
    public ArrayList<Order> getOrdersInTime(StatusOrder status, LocalDateTime startDate, LocalDateTime endDate, SortTypeOrder sortType) {
        ArrayList<Order> orders = orderDB.selectOrder(sortType);
        ArrayList<Order> newList = new ArrayList<>();
        for (Order order : orders) {
            if (order.getTimeStart().compareTo(endDate) <= 0 && order.getTimeComplete().compareTo(startDate) >= 0 && order.getStatus().equals(status)) {
                newList.add(order);
            }
        }
        return newList;
    }

    public ArrayList<Order> getOrderByMaster(String name) {
        Master master = masterDB.findMasterInDb(name);
        if (master != null) {
            ArrayList<OrderMaster> orderMasters = ordersByMasterDb.getOrdersByMasterInDB(master.getId());
            return carRepairOrderMaster.getOrderFromOrderMaster(orderMasters);
        }
        return new ArrayList<>();
    }

    public void exportOrder(){
        ArrayList<Order> orders = getListOfOrders(SortTypeOrder.ID);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
        ArrayList<String> dataList = new ArrayList<>(orders.size()+1);
        dataList.add("ID,NAME,COST,STATUS,CREATE_TIME,START_TIME,COMPLETE_TIME,PLACE\n");
        for(Order order : orders){
            int id = order.getId();
            String name = order.getName();
            int cost = order.getCost();
            String status = order.getStatus().getSTATUS();
            LocalDateTime createTime = order.getTimeCreate();
            String create = createTime.format(formatter);
            LocalDateTime startTime = order.getTimeStart();
            String start = startTime.format(formatter);
            LocalDateTime completeTime = order.getTimeComplete();
            String complete = completeTime.format(formatter);
            int placeId = order.getPlace().getId();
            dataList.add(id+","+name+","+cost+","+status+","+start+","+create+","+complete+","+placeId+"\n");
        }
        workWithFile.whereExport(dataList, config.getStandardFileCsvOrders());
    }
}


