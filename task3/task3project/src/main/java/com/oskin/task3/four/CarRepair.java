package com.oskin.task3.four;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CarRepair {
    private ArrayList<ObjectName> masters = new ArrayList<>();
    private ArrayList<ObjectName> garage = new ArrayList<>();
    private ArrayList<ObjectName> orders = new ArrayList<>();

    //Метод для удаления
    private void delete(String name, ArrayList<ObjectName> list) {
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getName())) {
                flag = true;
                System.out.println(list.get(i).getName() + " - удалено");
                list.remove(i);
                break;
            }
        }
        if (!flag) {
            System.out.println("Имя не найдено");
        }
    }

    //Добавить/удалить мастера
    void addMaster(String name) {
        Master master = new Master(name);
        masters.add(master);
        System.out.println("Мастер добавлен");
    }

    void deleteMaster(String name) {
        delete(name, masters);
    }

    //добавить/удалить место
    void addPlace(String name, int volume) {
        Place place = new Place(name, volume);
        garage.add(place);
        System.out.println("Место в гараже добавлено");
    }

    void deletePlace(String name) {
        delete(name, garage);
    }

    //Добавить/удалить/закрыть/отменить заказ
    void addOrder(String name, String customer, LocalDateTime timestart, LocalDateTime timecopmlete) {
        Order order = new Order(name, customer, timestart, timecopmlete);
        orders.add(order);
        System.out.println("Заказ добавлен");
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
}
