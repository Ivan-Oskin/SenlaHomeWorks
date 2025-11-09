package com.oskin.task4.one;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CarRepair {
    private ArrayList<Nameable> masters = new ArrayList<>();
    private ArrayList<Nameable> garage = new ArrayList<>();
    private ArrayList<Nameable> orders = new ArrayList<>();

    //Метод для поиска

    private <T extends Nameable> int findByName(String name, ArrayList<T> list) {
        int number = -1;
        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getName())) {
                number = i;
                break;
            }
        }
        return number;
    }

    // Метод для удаления

    private <T extends Nameable> void delete(String name, ArrayList<T> list) {
        int i = findByName(name, list);
        if (i == -1) {

        } else {
            //System.out.println(list.get(i).getName() + " - удалено");
            list.remove(i);
        }
    }

    //метод для сортировки

    private void sortOrders(ArrayList<Order> list, SortType sortType) {
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


    //Добавить/удалить мастера
    void addMaster(String name) {
        Master master = new Master(name);
        masters.add(master);
        //System.out.println("Мастер добавлен");
    }

    void deleteMaster(String name) {
        delete(name, masters);
    }

    //добавить/удалить место
    void addPlace(String name) {
        Place place = new Place(name);
        garage.add(place);
        //System.out.println("Место в гараже добавлено");
    }

    void deletePlace(String name) {
        delete(name, garage);
    }

    //Добавить/удалить/закрыть/отменить заказ
    void addOrder(String name, int cost, Place place, LocalDateTime timeCreate, LocalDateTime timeStart, LocalDateTime timeCopmlete) {
        Order order = new Order(name, cost, place, timeCreate, timeStart, timeCopmlete);
        orders.add(order);
        //System.out.println("Заказ добавлен");
    }

    void deleteOrder(String name) {
        delete(name, orders);
    }

    void completeOrder(String name) {
        boolean flag = false;
        for (int i = 0; i < orders.size(); i++) {
            if (name.equals(orders.get(i).getName())) {
                flag = true;
                Order order = (Order) orders.get(i);
                order.close();
                System.out.println(order.getName() + " закрыт");
                break;
            }
        }
        if (!flag) {
            System.out.println("Имя не найдено");
        }
    }

    void cancelOrder(String name) {
        boolean flag = false;
        for (int i = 0; i < orders.size(); i++) {
            if (name.equals(orders.get(i).getName())) {
                flag = true;
                Order order = (Order) orders.get(i);
                order.cancel();
                System.out.println(order.getName() + " отменён");
                break;
            }
        }
        if (!flag) {
            System.out.println("Имя не найдено");
        }
    }

    //метод для смещения времени
    private void offset(String name, int count, boolean isDay) {
        boolean flag = false;

        for (int i = 0; i < orders.size(); i++) {
            Order order = (Order) orders.get(i);
            if (name.equals(orders.get(i).getName())) {

                flag = true;
            }
            if (flag) {
                if (isDay) order.changeDay(count);
                else order.changeHour(count);
            }
        }
    }

    void offsetDay(String name, int countDay) {
        offset(name, countDay, true);
    }

    void offsetHour(String name, int countHour) {
        offset(name, countHour, false);
    }


    //4

    //список свободных мест в гараже

    public ArrayList<Place> getListOfPlace() {
        ArrayList<Place> newList = new ArrayList<>();
        for (int i = 0; i < garage.size(); i++) {
            newList.add((Place) garage.get(i));
        }
        return newList;
    }

    public ArrayList<Place> getFreePlace(LocalDateTime date) {
        ArrayList<Place> newList = new ArrayList<Place>();
        for (int i = 0; i < garage.size(); i++) {
            newList.add((Place) garage.get(i));
        }
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime = getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortType.START);
        for (int i = 0; i < ordersByTime.size(); i++) {
            if (ordersByTime.get(i).getTimeStart().compareTo(date) <= 0 && ordersByTime.get(i).getTimeComplete().compareTo(date) >= 0) {
                delete(ordersByTime.get(i).getPlace().getName(), newList);
            }
        }
        return newList;
    }


    //Список мастеров

    public void setOrderToMaster(String name, Order order) {
        int i = findByName(name, masters);
        if (i >= 0) {
            Master master = (Master) masters.get(i);
            master.addOrder(order);
        }
    }

    public ArrayList<Master> getListOfMasters(boolean sortByAlphabet) {
        ArrayList<Master> newList = new ArrayList<>();
        for (int i = 0; i < masters.size(); i++) {
            newList.add((Master) masters.get(i));
        }
        if (sortByAlphabet) {
            newList.sort(Comparator.comparing(Master::getName));
        } else {
            newList.sort(Comparator.comparing(Master::getCountOfOrders));
        }
        return newList;
    }

    //список заказов
    public ArrayList<Order> getListOfOrders(SortType sortType) {
        ArrayList<Order> newList = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            newList.add((Order) orders.get(i));
        }
        sortOrders(newList, sortType);
        return newList;
    }

    //Список текущих выполняемых заказов

    public ArrayList<Order> getListOfActiveOrders(SortType sortType) {
        ArrayList<Order> newList = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = (Order) orders.get(i);
            if (order.getStatus().equals(StatusOrder.ACTIVE)) {
                newList.add(order);
            }
        }
        sortOrders(newList, sortType);
        return newList;
    }

    //заказ, выполняемый конкретным мастером

    public ArrayList<Order> getOrderByMaster(String name) {
        ArrayList<Order> newList = new ArrayList<Order>();
        int i = findByName(name, masters);
        if (i >= 0) {
            Master master = (Master) masters.get(i);
            ArrayList<String> names = master.getNamesOfOrder();
            for (int j = 0; j < orders.size(); j++) {
                if (names.contains(orders.get(j).getName())) {
                    newList.add((Order) orders.get(j));
                }
            }
        }
        return newList;

    }

    //Мастера выполняющие конкретный заказ

    public ArrayList<Master> getMastersByOrder(String name) {
        ArrayList<Master> newList = new ArrayList<>();
        for (int i = 0; i < masters.size(); i++) {
            Master master = (Master) masters.get(i);
            if (master.getNamesOfOrder().contains(name)) {
                newList.add(master);
            }
        }
        return newList;
    }

    //Заказы за промежуток времени

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


    //Количество свободных мест на любую дату
    public int getCountFreeTime(LocalDateTime date) {
        int countPlace = getFreePlace(date).size();
        if (countPlace == 0) return 0;
        int countMaster = masters.size();
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime = getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortType.START);
        for (int i = 0; i < ordersByTime.size(); i++) {
            if (ordersByTime.get(i).getTimeStart().compareTo(date) <= 0 && ordersByTime.get(i).getTimeComplete().compareTo(date) >= 0) {
                countMaster -= getMastersByOrder(ordersByTime.get(i).getName()).size();
            }
        }
        //System.out.println(countMaster + " " + countPlace);
        if (countPlace > countMaster) {
            return countMaster;
        } else {
            return countPlace;
        }
    }

    public LocalDateTime getNearestDate(LocalDateTime fromDate) {
        LocalDateTime date = LocalDateTime.from(fromDate);
        while (true) {
            int k = getCountFreeTime(date);
            if (k > 0) {
                return date;
            } else {
                if (date.getDayOfWeek().getValue() >= 6) {
                    date = date.plusDays(8 - date.getDayOfWeek().getValue());
                    date = date.withHour(10);
                }
                if (date.getHour() < 10) {
                    date = date.withHour(10);
                } else if (date.getHour() >= 18) {
                    date = date.plusDays(1);
                    date = date.withHour(10);
                } else {
                    date = date.plusHours(1);
                }
            }

        }
    }
}



