package com.oskin.task5.View;

import com.oskin.task5.Controller.CarRepairController;
import com.oskin.task5.Model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CarRepairView {

    private static CarRepairView instance;
    private CarRepairView() {
    }

    public static CarRepairView getInstance() {
        if (instance == null) {
            instance = new CarRepairView();
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
        int i = 1;
        for (Order order : orders){
            System.out.println(i+". Заказ - "+order.getName()+":");
            System.out.println("Стоимость - "+order.getCost());
            System.out.println("Статус - "+order.getStatus());
            System.out.println("Место выполнения - "+order.getPlace().getName());
            System.out.println("Время создания - "+order.getTimeCreate());
            System.out.println("Время начала выполнения - "+order.getTimeStart());
            System.out.println("Время окночания выполнения - "+order.getTimeComplete());
            System.out.println("\n");
            i+=1;
        }
    }

    private <T extends Nameable> void printList(ArrayList<T> list){
        int i = 1;
        for(T object : list){
            System.out.println(i+". "+object.getName());
            i+=1;
        }
    }

    public void getListOrders(){
        printOrders(CarRepairOrders.getInstance().getListOfOrders(CarRepairController.getInstance().whatSortType()));
    }
    public void getFreePlace(){
        LocalDateTime time = CarRepairController.getInstance().createTime();
        printList(CarRepairGarage.getInstance().getFreePlace(time));
    }

    public void getMasters(){
        printList(CarRepairMaster.getInstance().getListOfMasters(CarRepairController.getInstance().SortAlphabet()));
    }

    public void getCurrentOrders(){
        printOrders(CarRepairOrders.getInstance().getListOfActiveOrders(CarRepairController.getInstance().whatSortType()));
    }

    public void getOrderByMaster(){
        String name = CarRepairController.getInstance().inputName("Имя мастера");
        printOrders(CarRepairOrders.getInstance().getOrderByMaster(name));
    }
    public void getMastersByOrder(){
        String name = CarRepairController.getInstance().inputName("Имя заказа");
        printList(CarRepairMaster.getInstance().getMastersByOrder(name));
    }
    public void getOrdersInTime(){
        System.out.println("Введите начальное время");
        LocalDateTime startTime = CarRepairController.getInstance().createTime();
        System.out.println("Введие конечное время");
        LocalDateTime endTime = CarRepairController.getInstance().createTime();
        StatusOrder status = CarRepairController.getInstance().whatStatus();
        SortType sortType = CarRepairController.getInstance().whatSortType();
        printOrders(CarRepairOrders.getInstance().getOrdersInTime(status, startTime, endTime, sortType));
    }
    public void getCountInTime(){
        LocalDateTime time = CarRepairController.getInstance().createTime();
        System.out.println("Количество свободных мест - "+CarRepair.getCountFreeTime(time));
    }
    public void getNearestDate(){
        LocalDateTime time = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        System.out.println("Ближайщее свободное время: "+CarRepair.getNearestDate(time));
    }


}
