package com.oskin.autoservice.View;

import com.oskin.Annotations.*;
import com.oskin.autoservice.DAO.OrderDB;
import com.oskin.autoservice.Model.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Singleton
public class CarRepairOutput {
    @Inject
    OrderDB orderDB;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");

    public CarRepairOutput() {
    }

    public void infAboutAdd(String nameObject) {
        System.out.println(nameObject + " Добавлен");
    }

    public void infAboutDelete(String nameObject, boolean inf) {
        if (inf) {
            System.out.println(nameObject + " Удален");
        } else {
            System.out.println(nameObject + " - Имя не найдено");
        }
    }

    public void NoFound() {
        System.out.println("Имя не найдено");
    }

    public void infAboutInput(String objectName) {
        System.out.println("Введите " + objectName);
    }

    public void printOrders(ArrayList<Order> orders) {
        for (Order order : orders) {
            System.out.println(order.getId() + " Заказ - " + order.getName() + ":");
            System.out.println("Стоимость - " + order.getCost());
            System.out.println("Статус - " + order.getStatus());
            System.out.println("Место выполнения - " + order.getPlace().getName());
            System.out.println("Время создания - " + order.getTimeCreate().format(formatter));
            System.out.println("Время начала выполнения - " + order.getTimeStart().format(formatter));
            System.out.println("Время окночания выполнения - " + order.getTimeComplete().format(formatter));
            System.out.println("\n");
        }
    }

    public void printMasters(ArrayList<Master> masters, boolean printOrder) {
        for (Master master : masters) {
            System.out.println(master.getId() + " " + master.getName());
            if (master.getCountOfOrdersId() > 0 && printOrder) {
                System.out.println("Заказы:");
                for (int idOrder : master.getIdOfOrder()) {
                    System.out.println(orderDB.findOrderInDB(idOrder).getName());
                }
                System.out.println();
            }
        }
    }
    public void printPlace(ArrayList<Place> places) {
        for (Place place : places) {
            System.out.println(place.getId() + " " + place.getName());
            System.out.println();
        }
    }
}