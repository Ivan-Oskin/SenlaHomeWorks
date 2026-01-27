package com.oskin.autoservice.controller;

import com.oskin.annotations.Inject;
import com.oskin.annotations.Singleton;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Singleton
public final class CarRepairOrders {
    @Inject
    OrderRepository orderRepository;
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;
    @Inject
    MasterRepository masterRepository;
    @Inject
    OrderMasterRepository orderMasterRepository;
    @Inject
    CarRepairOrderMaster carRepairOrderMaster;
    private final Logger logger = LoggerFactory.getLogger(CarRepairOrders.class);

    private static CarRepairOrders instance;

    private CarRepairOrders() {
    }

    public static CarRepairOrders getInstance() {
        if (instance == null) {
            instance = new CarRepairOrders();
        }
        return instance;
    }

    public void addOrder(int id, String name, int cost, Place place, LocalDateTime timeCreate, LocalDateTime timeStart, LocalDateTime timeCopmlete) {
        Order order = new Order(id, name, cost, place, timeCreate, timeStart, timeCopmlete);
        orderRepository.create(order);
    }

    public void addOrder(int id, String name, int cost, Place place, LocalDateTime timeCreate, LocalDateTime timeStart, LocalDateTime timeCopmlete, StatusOrder status) {
        Order order = new Order(id, name, cost, place, timeCreate, timeStart, timeCopmlete, status);
        orderRepository.create(order);
    }

    public boolean deleteOrder(String name) {
        return orderRepository.delete(name);
    }

    public boolean completeOrder(String name) {
        return orderRepository.changeStatusInDb(name, StatusOrder.CLOSE);
    }

    public boolean cancelOrder(String name) {
        return orderRepository.changeStatusInDb(name, StatusOrder.CANCEL);
    }

    public boolean offset(String name, int countDay, int countHour) {
        Order order = orderRepository.find(name);
        if (order == null) {
            System.out.println("не находит");
            return false;
        } else {
            LocalDateTime startTime = order.getTimeStart();
            LocalDateTime completeTime = order.getTimeComplete();
            LocalDateTime ChangeStartTime = startTime.plusDays(countDay);
            LocalDateTime ChangeCompleteTime = completeTime.plusDays(countDay);
            startTime = ChangeStartTime.plusHours(countHour);
            completeTime = ChangeCompleteTime.plusHours(countHour);
            return orderRepository.offsetInDb(name, startTime, completeTime);
        }
    }

    public ArrayList<Order> getListOfOrders(SortTypeOrder sortType) {
        ArrayList<Order> newList = orderRepository.findAll(sortType);
        return newList;
    }

    public ArrayList<Order> getListOfActiveOrders(SortTypeOrder sortType) {
        ArrayList<Order> orders = orderRepository.findAll(sortType);
        ArrayList<Order> newList = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus().equals(StatusOrder.ACTIVE)) {
                newList.add(order);
            }
        }
        return newList;
    }

    public ArrayList<Order> getOrdersInTime(StatusOrder status, LocalDateTime startDate, LocalDateTime endDate, SortTypeOrder sortType) {
        ArrayList<Order> orders = orderRepository.findAll(sortType);
        ArrayList<Order> newList = new ArrayList<>();
        for (Order order : orders) {
            if (order.getTimeStart().compareTo(endDate) <= 0 && order.getTimeComplete().compareTo(startDate) >= 0 && order.getStatus().equals(status)) {
                newList.add(order);
            }
        }
        return newList;
    }

    public ArrayList<Order> getOrderByMaster(String name) {
        Master master = masterRepository.find(name);
        if (master != null) {
            ArrayList<OrderMaster> orderMasters = orderMasterRepository.getOrdersByMasterInDB(master.getId());
            return carRepairOrderMaster.getOrderFromOrderMaster(orderMasters);
        }
        return new ArrayList<>();
    }

    public void exportOrder() {
        logger.info("Start export order");
        ArrayList<Order> orders = getListOfOrders(SortTypeOrder.ID);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
        ArrayList<String> dataList = new ArrayList<>(orders.size() + 1);
        dataList.add("ID,NAME,COST,STATUS,CREATE_TIME,START_TIME,COMPLETE_TIME,PLACE\n");
        for (Order order : orders) {
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
            dataList.add(id + "," + name + "," + cost + "," + status + "," + start + "," + create + "," + complete + "," + placeId + "\n");
        }
        workWithFile.whereExport(dataList, config.getStandardFileCsvOrders());
    }
}


