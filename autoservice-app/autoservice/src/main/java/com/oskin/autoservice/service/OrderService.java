package com.oskin.autoservice.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class OrderService {
    OrderRepository orderRepository;
    MasterRepository masterRepository;
    OrderMasterRepository orderMasterRepository;
    OrderMasterService orderMasterService;
    Config config;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(OrderRepository orderRepository, MasterRepository masterRepository, OrderMasterRepository orderMasterRepository,
                        OrderMasterService orderMasterService, Config config) {
        this.orderRepository = orderRepository;
        this.masterRepository = masterRepository;
        this.orderMasterRepository = orderMasterRepository;
        this.orderMasterService = orderMasterService;
        this.config = config;
    }
    @Transactional
    public void addOrder(int id, String name, int cost, Place place, LocalDateTime timeCreate, LocalDateTime timeStart, LocalDateTime timeComplete) {
        Order order = new Order(id, name, cost, place, timeCreate, timeStart, timeComplete);
        orderRepository.create(order);
    }
    @Transactional
    public void addOrder(int id, String name, int cost, Place place, LocalDateTime timeCreate, LocalDateTime timeStart, LocalDateTime timeComplete, StatusOrder status) {
        Order order = new Order(id, name, cost, place, timeCreate, timeStart, timeComplete, status);
        orderRepository.create(order);
    }
    @Transactional
    public boolean deleteOrder(String name) {
        Order order = orderRepository.find(name);
        if (order != null) {
            orderMasterRepository.deleteByOrder(order.getId());
            return orderRepository.delete(name);
        }
        return false;
    }

    public Order findOrder(int id) {
        return orderRepository.find(id);
    }
    @Transactional
    public void updateOrder(Order order) {
        orderRepository.update(order);
    }
    @Transactional
    public boolean completeOrder(String name) {
        return orderRepository.changeStatusInDb(name, StatusOrder.CLOSE);
    }
    @Transactional
    public boolean cancelOrder(String name) {
        return orderRepository.changeStatusInDb(name, StatusOrder.CANCEL);
    }
    @Transactional
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
        return orderRepository.findAll(sortType);
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
            if (!order.getTimeStart().isAfter(endDate) && !order.getTimeComplete().isBefore(startDate) && order.getStatus().equals(status)) {
                newList.add(order);
            }
        }
        return newList;
    }

    public ArrayList<Order> getOrderByMaster(String name) {
        Master master = masterRepository.find(name);
        if (master != null) {
            ArrayList<OrderMaster> orderMasters = orderMasterRepository.getOrdersByMasterInDB(master.getId());
            return orderMasterService.getOrderFromOrderMaster(orderMasters);
        }
        return new ArrayList<>();
    }
}


