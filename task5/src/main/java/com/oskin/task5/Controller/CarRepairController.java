package com.oskin.task5.Controller;
import com.oskin.task5.Model.*;
import com.oskin.task5.View.CarRepairView;
import java.time.LocalDateTime;
import java.util.Scanner;

public class CarRepairController {
    Scanner scanner = new Scanner(System.in);
    private static CarRepairController instance;
    private CarRepairController() {
    }
    public static CarRepairController getInstance() {
        if (instance == null) {
            instance = new CarRepairController();
        }
        return instance;
    }

    public SortType whatSortType(){
        System.out.println("Выберите порядок:\n 1. По дате подачи 2. По дате выполнения 3. По дате начала 4. по цене");
        int x;
        while (true){
            x = scanner.nextInt();
            scanner.nextLine();
            if(x > 0 && x < 5) break;
            System.out.println("Неправильный ввод");
        }
        if(x == 1) return SortType.CREATE;
        if(x == 2) return SortType.COMPLETE;
        if(x == 3) return SortType.START;
        return SortType.COST;
    }

    public boolean SortAlphabet(){
        System.out.println("Выберите порядок:\n1.По алфавиту 2.По занятности");
        int x = scanner.nextInt();
        scanner.nextLine();
        if(x == 1) return true;
        else return false;
    }
    public String inputName(String about){
        CarRepairView.getInstance().infAboutInput(about);
        return scanner.nextLine();
    }
    public LocalDateTime createTime() {

        CarRepairView.getInstance().infAboutInput("год");
        int year;
        int month;
        int day;
        int hour;
        while (true) {
            year = scanner.nextInt();
            scanner.nextLine();
            if (year >= LocalDateTime.now().getYear() && year < 2030) break;
            System.out.println("Неправильный ввод");
        }
        CarRepairView.getInstance().infAboutInput("Месяц");
        while (true) {
            month = scanner.nextInt();
            scanner.nextLine();
            if (month >= 1 && month <= 12) {
                break;
            }
            System.out.println("Неправильный ввод");
        }
        CarRepairView.getInstance().infAboutInput("День");
        while (true) {
            day = scanner.nextInt();
            scanner.nextLine();
            if (day >= 1 && day <= 31) {
                break;
            }
            System.out.println("Неправильный ввод");
        }
        CarRepairView.getInstance().infAboutInput("Часы");
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

    public StatusOrder whatStatus(){
        System.out.println("Выберите статус");
        System.out.println("1. активный 2. закрытый 3. отменённый ");
        int x;
        while (true){
            x = scanner.nextInt();
            scanner.nextLine();
            if(x > 0 && x < 4) break;
            System.out.println("Неправильный ввод");
        }
        if(x == 1) return StatusOrder.ACTIVE;
        if(x == 2) return StatusOrder.CLOSE;
        if(x == 3) return StatusOrder.CANCEL;
        return StatusOrder.ACTIVE;
    }

    public void addMaster() {
        String name = inputName("имя");
        CarRepairMaster.getInstance().addMaster(name);
        CarRepairView.getInstance().infAboutAdd(name);
    }

    public void addPlace() {
        String name = inputName("имя");
        CarRepairGarage.getInstance().addPlace(name);
        CarRepairView.getInstance().infAboutAdd(name);
    }

    public void addOrder() {
        String name = inputName("имя");
        CarRepairView.getInstance().infAboutInput("стоимость");
        int cost = scanner.nextInt();
        scanner.nextLine();
        String namePlace = inputName("наименование места");
        Place place = CarRepairGarage.getInstance().findPlace(namePlace);
        if (place == null) {
            System.out.println("Наименование места не найдено");
            return;
        }
        CarRepairView.getInstance().infAboutInput("Начало выполнения заказа: ");
        LocalDateTime timeStart = createTime();
        CarRepairView.getInstance().infAboutInput("Конец выполнения заказа");
        LocalDateTime timeComplete = createTime();

        CarRepairOrders.getInstance().addOrder(name, cost, place, LocalDateTime.now().withMinute(0), timeStart, timeComplete);

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
        String name = inputName("имя");
        CarRepairView.getInstance().infAboutInput("на сколько дней сместить заказ");
        int day = scanner.nextInt();
        scanner.nextLine();
        CarRepairView.getInstance().infAboutInput("на сколько часов сместить заказ");
        int hour = scanner.nextInt();
        scanner.nextLine();
        boolean inf = CarRepairOrders.getInstance().offsetDay(name, day);
        if (inf) {
            CarRepairOrders.getInstance().offsetHour(name, hour);
            System.out.println("Время заказа "+ name + " смещено");
        } else {
            CarRepairView.getInstance().NoFound();
        }
    }

    public void deleteMaster() {
        String name = inputName("имя");
        boolean inf = CarRepairMaster.getInstance().deleteMaster(name);
        CarRepairView.getInstance().infAboutDelete(name, inf);
    }
    public void deleteOrder() {
        String name = inputName("имя");
        boolean inf = CarRepairOrders.getInstance().deleteOrder(name);
        CarRepairView.getInstance().infAboutDelete(name, inf);
    }
    public void deletePlace() {
        String name = inputName("имя");
        boolean inf = CarRepairGarage.getInstance().deletePlace(name);
        CarRepairView.getInstance().infAboutDelete(name, inf);
    }

    public void setOrderToMaster(){
        String nameMaster = inputName("имя мастера");
        String nameOrder = inputName("название заказа");
        boolean inf = CarRepairMaster.getInstance().setOrderToMaster(nameMaster, nameOrder);
        if(inf) System.out.println("Заказ " + nameOrder +" добавлен к мастеру " + nameMaster);
        else System.out.print("не найдено");
    }
}
