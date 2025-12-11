package com.oskin.autoservice.View;

import com.oskin.DI.Singleton;
import com.oskin.autoservice.Controller.CarRepair;
import com.oskin.autoservice.Controller.CarRepairGarage;
import com.oskin.autoservice.Controller.CarRepairMaster;
import com.oskin.autoservice.Controller.CarRepairOrders;
import com.oskin.autoservice.Model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Singleton
public class CarRepairOutput {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
    private static CarRepairOutput instance;
    private CarRepairOutput() {
    }

    public static CarRepairOutput getInstance() {
        if (instance == null) {
            instance = new CarRepairOutput();
        }
        return instance;
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

    private void printOrders(ArrayList<Order> orders){
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

    private <T extends Nameable> void printList(ArrayList<T> list){
        int i = 1;
        for(T object : list){
            System.out.println(object.getId()+" "+object.getName());
            i+=1;
        }
    }

    public void getListOrders(){
        printOrders(CarRepairOrders.getInstance().getListOfOrders(CarRepairInput.getInstance().whatSortTypeOrder()));
    }
    public void getFreePlace(){
        LocalDateTime time = CarRepairInput.getInstance().createTime();
        if(time == null){
            System.out.println("произошла ошибка при вводе данных");
            return;
        }
        printList(CarRepairGarage.getInstance().getFreePlace(time));
    }

    public void getMasters(){
        printList(CarRepairMaster.getInstance().getListOfMasters(CarRepairInput.getInstance().whatSortTypeMaster()));
    }

    public void getCurrentOrders(){
        printOrders(CarRepairOrders.getInstance().getListOfActiveOrders(CarRepairInput.getInstance().whatSortTypeOrder()));
    }

    public void getOrderByMaster(){
        String name = CarRepairInput.getInstance().inputName("Имя мастера");
        printOrders(CarRepairOrders.getInstance().getOrderByMaster(name));
    }
    public void getMastersByOrder(){
        String name = CarRepairInput.getInstance().inputName("Имя заказа");
        printList(CarRepairMaster.getInstance().getMastersByOrder(name));
    }
    public void getOrdersInTime(){
        System.out.println("Введите начальное время");
        LocalDateTime startTime = CarRepairInput.getInstance().createTime();
        if(startTime == null){
            System.out.println("произошла ошибка при вводе данных");
            return;
        }
        System.out.println("Введие конечное время");
        LocalDateTime endTime = CarRepairInput.getInstance().createTime();
        if(endTime == null){
            System.out.println("произошла ошибка при вводе данных");
            return;
        }
        StatusOrder status = CarRepairInput.getInstance().whatStatus();
        SortTypeOrder sortType = CarRepairInput.getInstance().whatSortTypeOrder();
        printOrders(CarRepairOrders.getInstance().getOrdersInTime(status, startTime, endTime, sortType));
    }
    public void getCountInTime(){
        LocalDateTime time = CarRepairInput.getInstance().createTime();
        if(time == null){
            System.out.println("произошла ошибка при вводе данных");
            return;
        }
        System.out.println("Количество свободных мест - "+ CarRepair.getCountFreeTime(time));
    }
    public void getNearestDate(){
        LocalDateTime time = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime nearestDate = CarRepair.getNearestDate(time);
        if(nearestDate == null){
            System.out.println("Ошибка! Отсуствует список мастеров или заказов");
            return;
        }
        System.out.println("Ближайщее свободное время: "+nearestDate.format(formatter));
    }


}
