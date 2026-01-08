package com.oskin.autoservice.Controller;
import com.oskin.Annotations.*;
import com.oskin.autoservice.DAO.MasterDB;
import com.oskin.autoservice.DAO.OrderDB;
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
    CarRepair carRepair;
    @Inject
    Config config;
    @Inject
    CarRepairMaster carRepairMaster;
    @Inject
    MasterDB masterDB;

    private static CarRepairOrders instance;

    private CarRepairOrders(){}

    public static CarRepairOrders getInstance(){
        if(instance == null){
            instance = new CarRepairOrders();
        }
        return instance;
    }
    private static void sortOrders(ArrayList<Order> list, SortTypeOrder sortType) {
        switch (sortType) {
            case CREATE:
                list.sort(Comparator.comparing(Order::getTimeCreate));
                break;
            case START:
                list.sort(Comparator.comparing(Order::getTimeStart));
                break;
            case COMPLETE:
                list.sort(Comparator.comparing(Order::getTimeComplete));
                break;
            case COST:
                list.sort(Comparator.comparing(Order::getCost));
                break;
            case ID:
                list.sort(Comparator.comparing(Order::getId));
        }
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
        ArrayList<Order> newList = orderDB.selectOrder();
        sortOrders(newList, sortType);
        return newList;
    }

    public ArrayList<Order> getListOfActiveOrders(SortTypeOrder sortType) {
        ArrayList<Order> orders = orderDB.selectOrder();
        ArrayList<Order> newList = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus().equals(StatusOrder.ACTIVE)) {
                newList.add(order);
            }
        }
        sortOrders(newList, sortType);
        return newList;
    }
    public ArrayList<Order> getOrdersInTime(StatusOrder status, LocalDateTime startDate, LocalDateTime endDate, SortTypeOrder sortType) {
        ArrayList<Order> orders = orderDB.selectOrder();
        ArrayList<Order> newList = new ArrayList<>();
        for (Order order : orders) {
            if (order.getTimeStart().compareTo(endDate) <= 0 && order.getTimeComplete().compareTo(startDate) >= 0 && order.getStatus().equals(status)) {
                newList.add(order);
            }
        }
        sortOrders(newList, sortType);
        return newList;
    }

    public ArrayList<Order> getOrderByMaster(String name) {
        Master master = masterDB.SelectMasterByName(name);
        ArrayList<Order> Orders = new ArrayList<>();
        if (master != null) {
            for(int OrderId : master.getIdOfOrder()){
                Orders.add(orderDB.findOrderInDB(OrderId));
            }
        }
        return Orders;
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
            String placeName = order.getPlace().getName();
            dataList.add(id+","+name+","+cost+","+status+","+start+","+create+","+complete+","+placeName+"\n");
        }
        workWithFile.whereExport(dataList, config.getStandartFileCsvOrders());
    }
}


