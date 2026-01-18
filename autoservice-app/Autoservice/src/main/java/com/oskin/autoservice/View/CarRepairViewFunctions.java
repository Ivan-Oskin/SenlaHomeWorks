package com.oskin.autoservice.View;

import com.oskin.Annotations.*;
import com.oskin.autoservice.Controller.*;
import com.oskin.autoservice.Model.*;
import com.oskin.config.Config;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Singleton
public class CarRepairViewFunctions {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
    @Inject
    CarRepairGarage carRepairGarage;
    @Inject
    CarRepairOrders carRepairOrders;
    @Inject
    CarRepairMaster carRepairMaster;
    @Inject
    CarRepairOutput carRepairOutput;
    @Inject
    CarRepairInput carRepairInput;
    @Inject
    Config config;
    @Inject
    CarRepairDate carRepairDate;

    public void getListOrders(){
        carRepairOutput.printOrders(carRepairOrders.getListOfOrders(carRepairInput.whatSortTypeOrder()));
    }
    public void getFreePlace(){
        LocalDateTime time = carRepairInput.createTime();
        if(time == null){
            System.out.println("произошла ошибка при вводе данных");
            return;
        }
        carRepairOutput.printPlace(carRepairGarage.getFreePlace(time));
    }

    public void getMasters(){
        carRepairOutput.printMasters(carRepairMaster.getListOfMasters(carRepairInput.whatSortTypeMaster()), true);
    }

    public void getCurrentOrders(){
        carRepairOutput.printOrders(carRepairOrders.getListOfActiveOrders(carRepairInput.whatSortTypeOrder()));
    }

    public void getOrderByMaster(){
        String name = carRepairInput.inputName("Имя мастера");
        carRepairOutput.printOrders(carRepairOrders.getOrderByMaster(name));
    }
    public void getMastersByOrder(){
        String name = carRepairInput.inputName("Имя заказа");
        carRepairOutput.printMasters(carRepairMaster.getMastersByOrder(name), false);
    }
    public void getOrdersInTime(){
        System.out.println("Введите начальное время");
        LocalDateTime startTime = carRepairInput.createTime();
        if(startTime == null){
            System.out.println("произошла ошибка при вводе данных");
            return;
        }
        System.out.println("Введие конечное время");
        LocalDateTime endTime = carRepairInput.createTime();
        if(endTime == null){
            System.out.println("произошла ошибка при вводе данных");
            return;
        }
        StatusOrder status = carRepairInput.whatStatus();
        SortTypeOrder sortType = carRepairInput.whatSortTypeOrder();
        carRepairOutput.printOrders(carRepairOrders.getOrdersInTime(status, startTime, endTime, sortType));
    }
    public void getCountInTime(){
        LocalDateTime time = carRepairInput.createTime();
        if(time == null){
            System.out.println("произошла ошибка при вводе данных");
            return;
        }
        System.out.println("Количество свободных мест - "+ carRepairDate.getCountFreeTime(time));
    }
    public void getNearestDate(){
        LocalDateTime time = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime nearestDate = carRepairDate.getNearestDate(time);
        if(nearestDate == null){
            System.out.println("Ошибка! Отсуствует список мастеров или заказов");
            return;
        }
        System.out.println("Ближайщее свободное время: "+nearestDate.format(formatter));
    }

    public void addMaster() {
        String name = carRepairInput.inputName("имя");
        ArrayList<Master> list = carRepairMaster.getListOfMasters(SortTypeMaster.ID);
        int id = !list.isEmpty()?((list.get(list.size()-1).getId())+1):1;
        carRepairMaster.addMaster(id ,name);
        carRepairOutput.infAboutAdd(name);
    }

    public void addPlace() {
        if(!config.getRuleAddPlace()){
            System.err.println("Запрещено");
            return;
        }
        String name = carRepairInput.inputName("имя");
        ArrayList<Place> list = carRepairGarage.getListOfPlace();
        int id = !list.isEmpty()?list.get(list.size()-1).getId()+1:1;
        carRepairGarage.addPlace(id, name);
        carRepairOutput.infAboutAdd(name);
    }

    public void addOrder() {
        String name = carRepairInput.inputName("имя");
        carRepairOutput.infAboutInput("стоимость");
        int cost = 0;
        while (cost<=0){
            cost = carRepairInput.inputInt();
            if(cost <= 0) System.out.println("Введите число больше 0");
        }
        String namePlace = carRepairInput.inputName("наименование места");
        Place place = carRepairGarage.findPlace(namePlace);
        if (place == null) {
            System.out.println("Наименование места не найдено");
            return;
        }
        LocalDateTime timeStart;
        LocalDateTime timeComplete;
        carRepairOutput.infAboutInput("Начало выполнения заказа: ");
        timeStart = carRepairInput.createTime();
        if(timeStart == null){
            System.out.println("Произошла ошибка при вводе данных");
            return;
        }
        carRepairOutput.infAboutInput("Конец выполнения заказа");
        timeComplete = carRepairInput.createTime();
        if(timeComplete == null){
            System.out.println("Произошла ошибка при вводе данных");
            return;
        }
        ArrayList<Order> list = carRepairOrders.getListOfOrders(SortTypeOrder.ID);
        int id = !list.isEmpty()?list.get(list.size()-1).getId()+1:1;
        carRepairOrders.addOrder(id, name, cost, place, LocalDateTime.now().withMinute(0), timeStart, timeComplete);
    }

    public void cancelOrder() {
        String name = carRepairInput.inputName("имя");
        boolean inf = carRepairOrders.cancelOrder(name);
        if (inf) {
            System.out.println(name + " отменен");
        } else {
            System.out.println("Имя не найдено");
        }
    }

    public void completeOrder() {
        String name = carRepairInput.inputName("имя");
        boolean inf = carRepairOrders.completeOrder(name);
        if (inf) {
            System.out.println(name + " закрыт");
        } else {
            System.out.println("Имя не найдено");
        }
    }

    public void offsetTimeOrder() {
        if(!config.getRuleOffset()){
            System.err.println("Запрещено");
            return;
        }
        String name = carRepairInput.inputName("имя");
        carRepairOutput.infAboutInput("на сколько дней сместить заказ");
        int day = carRepairInput.inputInt();
        carRepairOutput.infAboutInput("на сколько часов сместить заказ");
        int hour = carRepairInput.inputInt();
        boolean inf = carRepairOrders.offset(name, day, hour);
        if(inf){
            System.out.println(name +" - время выполнения смещено");
        }else {
            System.out.println(name + " - не удалось смести время");
        }
    }

    public void deleteMaster() {
        String name = carRepairInput.inputName("имя");
        boolean inf = carRepairMaster.deleteMaster(name);
        carRepairOutput.infAboutDelete(name, inf);
    }
    public void deleteOrder() {
        if(!config.getRuleDeleteOrder()){
            System.err.println("Запрещено");
            return;
        }
        String name = carRepairInput.inputName("имя");
        boolean inf = carRepairOrders.deleteOrder(name);
        carRepairOutput.infAboutDelete(name, inf);
    }

    public void deletePlace() {
        if(!config.getRuleDeletePlace()){
            System.err.println("Запрещено");
            return;
        }
        String name = carRepairInput.inputName("имя");
        boolean inf = carRepairGarage.deletePlace(name);
        carRepairOutput.infAboutDelete(name, inf);
    }

    public void setOrderToMaster(){
        String nameMaster = carRepairInput.inputName("имя мастера");
        String nameOrder = carRepairInput.inputName("название заказа");
        boolean inf = carRepairMaster.setOrderToMaster(nameMaster, nameOrder);
        if(inf) System.out.println("Заказ " + nameOrder +" добавлен к мастеру " + nameMaster);
        else System.out.println("не получилось добавить заказ мастеру ");
    }
}
