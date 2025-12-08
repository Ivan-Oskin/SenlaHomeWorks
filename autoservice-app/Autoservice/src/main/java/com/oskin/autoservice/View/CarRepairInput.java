package com.oskin.autoservice.View;
import com.oskin.autoservice.Controller.CarRepairGarage;
import com.oskin.autoservice.Controller.CarRepairMaster;
import com.oskin.autoservice.Controller.CarRepairOrders;
import com.oskin.autoservice.Controller.Configuration;
import com.oskin.autoservice.Model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CarRepairInput {
    Scanner scanner = new Scanner(System.in);
    private static CarRepairInput instance;
    private CarRepairInput() {
    }
    public static CarRepairInput getInstance() {
        if (instance == null) {
            instance = new CarRepairInput();
        }
        return instance;
    }
    public int inputInt(){
        int input = 0;
        try{
            input = scanner.nextInt();
            scanner.nextLine();
        }
        catch (InputMismatchException e){
            scanner.nextLine();
            System.err.println("\nНадо ввести только цифру!!!\n");
        }
        return input;
    }

    public SortTypeOrder whatSortTypeOrder(){
        System.out.println("Выберите порядок:\n1.По ID 2. По дате подачи 3. По дате начала 4.По дате выполнения 5. по цене");
        int x;
        while (true){
            x = inputInt();
            if(x > 0 && x < 6) break;
            System.out.println("Неправильный ввод");
        }
        if(x == 1) return SortTypeOrder.ID;
        if(x == 2) return SortTypeOrder.CREATE;
        if(x == 3) return SortTypeOrder.START;
        if(x == 4) return SortTypeOrder.COMPLETE;
        return SortTypeOrder.COST;
    }

    public SortTypeMaster whatSortTypeMaster(){
        System.out.println("Выберите порядок:\n1.По ID 2.По алфавиту 3.По занятности");
        int x;
        while (true){
            x = inputInt();
            if(x > 0 && x < 4) break;
            System.out.println("Неправильный ввод");
        }
        if(x == 1) return SortTypeMaster.ID;
        if(x == 2) return SortTypeMaster.ALPHABET;
        return SortTypeMaster.BUSYNESS;
    }
    public String inputName(String about){
        CarRepairOutput.getInstance().infAboutInput(about);
        return scanner.nextLine();
    }
    public LocalDateTime createTime() {

        CarRepairOutput.getInstance().infAboutInput("год");
        int year;
        int month;
        int day;
        int hour;
        try{
            while (true) {
                year = scanner.nextInt();
                scanner.nextLine();
                if (year >= LocalDateTime.now().getYear() && year < 2030) break;
                System.out.println("Неправильный ввод");
            }
            CarRepairOutput.getInstance().infAboutInput("Месяц");
            while (true) {
                month = scanner.nextInt();
                scanner.nextLine();
                if (month >= 1 && month <= 12) {
                    break;
                }
                System.out.println("Неправильный ввод");
            }
            CarRepairOutput.getInstance().infAboutInput("День");
            while (true) {
                day = scanner.nextInt();
                scanner.nextLine();
                if (day >= 1 && day <= 31) {
                    break;
                }
                System.out.println("Неправильный ввод");
            }
            CarRepairOutput.getInstance().infAboutInput("Часы");
            while (true) {
                hour = scanner.nextInt();
                scanner.nextLine();
                if (hour >= 9 && hour < 18) {
                    break;
                }
                System.out.println("Неправильный ввод");
            }
            LocalDateTime time = LocalDateTime.of(year, month, day, hour, 0);
            return time;
        }
        catch (InputMismatchException e){
            scanner.nextLine();
            System.err.println("\nНеправильный ввод, нужно вводить только цифры\n");
            return null;
        }
    }

    public StatusOrder whatStatus(){
        System.out.println("Выберите статус");
        System.out.println("1. активный 2. закрытый 3. отменённый ");
        int x;
        while (true){
            x = inputInt();
            if(x > 0 && x < 4) break;
            System.out.println("Неправильный ввод");
        }
        if(x == 1) return StatusOrder.ACTIVE;
        if(x == 2) return StatusOrder.CLOSE;
        return StatusOrder.CANCEL;
    }

    public void addMaster() {
        String name = inputName("имя");
        ArrayList<Master> list = CarRepairMaster.getInstance().getListOfMasters(SortTypeMaster.ID);
        int id = !list.isEmpty()?((list.get(list.size()-1).getId())+1):1;
        CarRepairMaster.getInstance().addMaster(id ,name);
        CarRepairOutput.getInstance().infAboutAdd(name);
    }

    public void addPlace() {
        if(!Configuration.getInstance().getRulePlaceAdd()){
            System.err.println("Запрещено");
            return;
        }
        String name = inputName("имя");
        ArrayList<Place> list = CarRepairGarage.getInstance().getListOfPlace();
        int id = !list.isEmpty()?list.get(list.size()-1).getId()+1:1;
        CarRepairGarage.getInstance().addPlace(id, name);
        CarRepairOutput.getInstance().infAboutAdd(name);
    }

    public void addOrder() {
        String name = inputName("имя");
        CarRepairOutput.getInstance().infAboutInput("стоимость");
        int cost = 0;
        while (cost<=0){
            cost = inputInt();
            if(cost <= 0) System.out.println("Введите число больше 0");
        }
        String namePlace = inputName("наименование места");
        Place place = CarRepairGarage.getInstance().findPlace(namePlace);
        if (place == null) {
            System.out.println("Наименование места не найдено");
            return;
        }
        LocalDateTime timeStart;
        LocalDateTime timeComplete;
        CarRepairOutput.getInstance().infAboutInput("Начало выполнения заказа: ");
        timeStart = createTime();
        if(timeStart == null){
            System.out.println("Произошла ошибка при вводе данных");
            return;
        }
        CarRepairOutput.getInstance().infAboutInput("Конец выполнения заказа");
        timeComplete = createTime();
        if(timeComplete == null){
            System.out.println("Произошла ошибка при вводе данных");
            return;
        }
        ArrayList<Order> list = CarRepairOrders.getInstance().getListOfOrders(SortTypeOrder.ID);
        int id = !list.isEmpty()?list.get(list.size()-1).getId()+1:1;
        CarRepairOrders.getInstance().addOrder(id, name, cost, place, LocalDateTime.now().withMinute(0), timeStart, timeComplete);
    }

    public void cancelOrder() {
        String name = inputName("имя");
        boolean inf = CarRepairOrders.getInstance().cancelOrder(name);
        if (inf) {
            System.out.println(name + " отменен");
        } else {
            System.out.println("Имя не найдено");
        }
    }

    public void completeOrder() {
        String name = inputName("имя");
        boolean inf = CarRepairOrders.getInstance().completeOrder(name);
        if (inf) {
            System.out.println(name + " закрыт");
        } else {
            System.out.println("Имя не найдено");
        }
    }

    public void offsetTimeOrder() {
        if(!Configuration.getInstance().getRuleOrderOffset()){
            System.err.println("Запрещено");
            return;
        }
        String name = inputName("имя");
        CarRepairOutput.getInstance().infAboutInput("на сколько дней сместить заказ");
        int day = inputInt();
        CarRepairOutput.getInstance().infAboutInput("на сколько часов сместить заказ");
        int hour = inputInt();
        boolean inf = CarRepairOrders.getInstance().offsetDay(name, day);
        if (inf) {
            CarRepairOrders.getInstance().offsetHour(name, hour);
            System.out.println("Время заказа "+ name + " смещено");
        } else {
            CarRepairOutput.getInstance().NoFound();
        }
    }

    public void deleteMaster() {
        String name = inputName("имя");
        boolean inf = CarRepairMaster.getInstance().deleteMaster(name);
        CarRepairOutput.getInstance().infAboutDelete(name, inf);
    }
    public void deleteOrder() {
        if(!Configuration.getInstance().getRuleOrderDelete()){
            System.err.println("Запрещено");
            return;
        }
        String name = inputName("имя");
        boolean inf = CarRepairOrders.getInstance().deleteOrder(name);
        CarRepairOutput.getInstance().infAboutDelete(name, inf);
    }
    public void deletePlace() {
        if(!Configuration.getInstance().getRulePlaceDelete()){
            System.err.println("Запрещено");
            return;
        }
        String name = inputName("имя");
        boolean inf = CarRepairGarage.getInstance().deletePlace(name);
        CarRepairOutput.getInstance().infAboutDelete(name, inf);
    }

    public void setOrderToMaster(){
        String nameMaster = inputName("имя мастера");
        String nameOrder = inputName("название заказа");
        boolean inf = CarRepairMaster.getInstance().setOrderToMaster(nameMaster, nameOrder);
        if(inf) System.out.println("Заказ " + nameOrder +" добавлен к мастеру " + nameMaster);
        else System.out.print("не найдено");
    }
}
