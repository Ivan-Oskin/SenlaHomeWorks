package com.oskin.task5.Model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class CarRepairOrders {

    private static CarRepairOrders instance;

    private CarRepairOrders(){}

    public static CarRepairOrders getInstance(){
        if(instance == null){
            instance = new CarRepairOrders();
        }
        return instance;
    }

    private ArrayList<Order> orders = new ArrayList<>();

    private static void sortOrders(ArrayList<Order> list, SortType sortType) {
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
        }
    }

    public void addOrder(String name, int cost, Place place, LocalDateTime timeCreate, LocalDateTime timeStart, LocalDateTime timeCopmlete) {
        Order order = new Order(name, cost, place, timeCreate, timeStart, timeCopmlete);
        orders.add(order);
    }
    public boolean deleteOrder(String name) {
        return CarRepair.delete(name, orders);
    }

    public boolean completeOrder(String name) {
        int i = CarRepair.findByName(name, orders);
        if (i == -1) {
            return false;
        } else {
            Order order =  orders.get(i);
            order.close();
            return true;
        }
    }

    public boolean cancelOrder(String name) {
        int i = CarRepair.findByName(name, orders);
        if (i == -1) {
            return false;
        } else {
            Order order = orders.get(i);
            order.cancel();
            return true;
        }
    }

    private boolean offset(String name, int count, boolean isDay) {
        boolean flag = false;
        orders.sort(Comparator.comparing(Order::getTimeStart));
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (name.equals(orders.get(i).getName())) {

                flag = true;
            }
            if (flag) {
                if (isDay) order.changeDay(count);
                else order.changeHour(count);
            }
        }
        return flag;
    }

    public boolean offsetDay(String name, int countDay) {
        return offset(name, countDay, true);

    }

    public boolean offsetHour(String name, int countHour) {
        return offset(name, countHour, false);
    }

    public ArrayList<Order> getListOfOrders(SortType sortType) {
        ArrayList<Order> newList = new ArrayList<>(orders);
        sortOrders(newList, sortType);
        return newList;
    }

    public ArrayList<Order> getListOfActiveOrders(SortType sortType) {
        ArrayList<Order> newList = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getStatus().equals(StatusOrder.ACTIVE)) {
                newList.add(order);
            }
        }
        sortOrders(newList, sortType);
        return newList;
    }

    public ArrayList<Order> getOrdersInTime(StatusOrder status, LocalDateTime startDate, LocalDateTime endDate, SortType sortType) {
        ArrayList<Order> newList = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = (Order) orders.get(i);
            if (order.getTimeStart().compareTo(endDate) <= 0 && order.getTimeComplete().compareTo(startDate) >= 0 && order.getStatus().equals(status)) {
                newList.add(order);
            }
        }
        sortOrders(newList, sortType);
        return newList;
    }

    public ArrayList<Order> getOrderByMaster(String name) {
        ArrayList<Order> newList = new ArrayList<>();
        ArrayList<Master> listOfMasters = CarRepairMaster.getInstance().getListOfMasters(true);
        int i = CarRepair.findByName(name, listOfMasters);
        if (i >= 0) {
            Master master = listOfMasters.get(i);
            ArrayList<String> names = master.getNamesOfOrder();
            for (int j = 0; j < orders.size(); j++) {
                if (names.contains(orders.get(j).getName())) {
                    newList.add( orders.get(j));
                }
            }
        }
        return newList;

    }
}


