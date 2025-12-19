package com.oskin.autoservice.View;

import com.oskin.Annotations.*;
import com.oskin.autoservice.Model.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Singleton
public class CarRepairOutput {
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
    public void infAboutInput(String objectName){
        System.out.println("Введите "+ objectName);
    }

    public void printOrders(ArrayList<Order> orders){
        for (Order order : orders){
            System.out.println(order.getId()+" Заказ - "+order.getName()+":");
            System.out.println("Стоимость - "+order.getCost());
            System.out.println("Статус - "+order.getStatus());
            System.out.println("Место выполнения - "+order.getPlace().getName());
            System.out.println("Время создания - "+order.getTimeCreate().format(formatter));
            System.out.println("Время начала выполнения - "+order.getTimeStart().format(formatter));
            System.out.println("Время окночания выполнения - "+order.getTimeComplete().format(formatter));
            System.out.println("\n");
        }
    }

    public  <T extends Nameable> void printList(ArrayList<T> list){
        int i = 1;
        for(T object : list){
            System.out.println(object.getId()+" "+object.getName());
            i+=1;
        }
    }


}
