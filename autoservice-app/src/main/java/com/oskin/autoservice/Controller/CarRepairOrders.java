package com.oskin.autoservice.Controller;
import com.oskin.autoservice.Model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public ArrayList<Order> getListOfOrders(SortTypeOrder sortType) {
        ArrayList<Order> newList = new ArrayList<>(orders);
        sortOrders(newList, sortType);
        return newList;
    }

    public ArrayList<Order> getListOfActiveOrders(SortTypeOrder sortType) {
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

    public ArrayList<Order> getOrdersInTime(StatusOrder status, LocalDateTime startDate, LocalDateTime endDate, SortTypeOrder sortType) {
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
        ArrayList<Master> listOfMasters = CarRepairMaster.getInstance().getListOfMasters(SortTypeMaster.ID);
        int i = CarRepair.findByName(name, listOfMasters);
        if (i >= 0) {
            Master master = listOfMasters.get(i);
            ArrayList<String> names = master.getNamesOfOrder();
            for (int j = 0; j < orders.size(); j++) {
                if (names.contains(orders.get(j).getName())) {
                    newList.add(orders.get(j));
                }
            }
        }
        return newList;

    }

    public void exportOrder(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
        ArrayList<String> dataList = new ArrayList<>(orders.size()+1);
        dataList.add("ID,NAME,COST,STATUS,CREATE_TIME,START_TIME,COMPLETE_TIME,PLACE\n");
        for(Order order : getListOfOrders(SortTypeOrder.ID)){
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
        CarRepair.whereExport(dataList, FileName.ORDER);
    }

    public void importOrder(){
        String nameFile = CarRepair.whereFromImport(FileName.ORDER);
        if(nameFile.equals("???")){
            return;
        }
        ArrayList<ArrayList<String>> data = CarRepair.importData(nameFile);
        if(!data.isEmpty()){
            for(ArrayList<String> line : data){
                if(line.size() != 8){
                    System.out.println("Неправильная таблица данных");
                    return;
                }
                else {
                    int id;
                    String name;
                    int cost;
                    StatusOrder status = null;
                    LocalDateTime create;
                    LocalDateTime start;
                    LocalDateTime complete;
                    Place place;
                    try {
                        id = Integer.parseInt(line.get(0));
                        name = line.get(1);
                        cost = Integer.parseInt(line.get(2));
                        String statusString = line.get(3);
                        for(StatusOrder statusOrder : StatusOrder.values()){
                            if(statusOrder.getSTATUS().equals(statusString)){
                                status = statusOrder;
                                break;
                            }
                        }
                        if(status == null){
                            System.err.println("Неправильные данные в заказе "+name);
                            continue;
                        }
                        String namePlace = line.get(7);
                        ArrayList<Place> listPlace = CarRepairGarage.getInstance().getListOfPlace();
                        int findPlace = CarRepair.findByName(namePlace, listPlace);
                        if(findPlace > -1){
                            place = listPlace.get(findPlace);
                        }
                        else {
                            System.out.println("место "+namePlace+" Не найдено.");
                            System.out.println("Заказ "+name + " не будет добавлен");
                            continue;
                        }
                    } catch (NumberFormatException e){
                        System.err.println("Неправильные данные");
                        return;
                    }
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
                        create = LocalDateTime.parse(line.get(4), formatter);
                        start = LocalDateTime.parse(line.get(5), formatter);
                        complete = LocalDateTime.parse(line.get(6), formatter);
                    }
                    catch (DateTimeParseException e){
                        System.err.println("произошла ошибка при парсинге времени заказа "+name);
                        continue;
                    }
                    int findOrder = CarRepair.findById(id, orders);
                    if(findOrder > -1){
                        orders.set(findOrder, new Order(id, name, cost, place, create, start, complete));
                    }
                    else{
                        addOrder(id, name, cost, place, create, start, complete);
                    }
                }
            }
        }
    }
}


