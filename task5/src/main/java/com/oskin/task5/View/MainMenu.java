package com.oskin.task5.View;

import com.oskin.task5.Controller.CarRepairController;

import java.util.Scanner;

public class MainMenu {
    private static Builder builder = new Builder();
    private static Navigator navigator = new Navigator();
    private static MainMenu instance;

    private MainMenu(){}

    public static MainMenu getInstance(){
        if(instance == null){
            instance = new MainMenu();
        }
        return instance;
    }

    public static void run(){
        Scanner scanner = new Scanner(System.in);
        builder.setTitle("Добавить данные");
        builder.addItem(1, "Добавить мастера",() -> CarRepairController.getInstance().addMaster());
        builder.addItem(2, "Добавить место", () -> CarRepairController.getInstance().addPlace());
        builder.addItem(3, "Добавить заказ", ()-> CarRepairController.getInstance().addOrder());
        builder.addItem(4, "Добавить заказ мастеру", () -> CarRepairController.getInstance().setOrderToMaster());
        navigator.addMenu(builder.build());
        builder.setTitle("Удалить данные");
        builder.addItem(1, "Удалить мастера", () -> CarRepairController.getInstance().deleteMaster());
        builder.addItem(2, "Удалить место", () -> CarRepairController.getInstance().deletePlace());
        builder.addItem(3, "Удалить место", () -> CarRepairController.getInstance().deleteOrder());
        navigator.addMenu(builder.build());
        builder.setTitle("Изменить данные");
        builder.addItem(1, "Сместить время выполнения закзазов", () -> CarRepairController.getInstance().offsetTimeOrder());
        builder.addItem(2, "Завершить заказ", () -> CarRepairController.getInstance().completeOrder());
        builder.addItem(3, "Отменить заказ", () -> CarRepairController.getInstance().cancelOrder());
        navigator.addMenu(builder.build());
        builder.setTitle("Получить данные");
        builder.addItem(1, "Свободные места", () -> CarRepairView.getInstance().getFreePlace());
        builder.addItem(2, "Список заказов", () -> CarRepairView.getInstance().getListOrders());
        builder.addItem(3, "Список мастеров", () -> CarRepairView.getInstance().getMasters());
        builder.addItem(4, "Текущие заказы", () -> CarRepairView.getInstance().getCurrentOrders());
        builder.addItem(5, "Заказы мастера", () -> CarRepairView.getInstance().getOrderByMaster());
        builder.addItem(6, "Мастера, выполняющие заказ", () -> CarRepairView.getInstance().getMastersByOrder());
        builder.addItem(7, "Заказы за определенный период", () -> CarRepairView.getInstance().getOrdersInTime());
        builder.addItem(8, "Количество свободных мест", () -> CarRepairView.getInstance().getCountInTime());
        builder.addItem(9, "Ближайшая свободна дата", () -> CarRepairView.getInstance().getNearestDate());
        navigator.addMenu(builder.build());



       while (true){
           System.out.println("Введите действие: ");
           navigator.getNamesOfMenu();
           int x = scanner.nextInt();
           scanner.nextLine();
           if(x == 0) return;
           navigator.navigate(x);
       }
    }
}
