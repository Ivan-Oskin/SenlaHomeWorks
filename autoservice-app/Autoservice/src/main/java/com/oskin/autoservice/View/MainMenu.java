package com.oskin.autoservice.View;

import com.oskin.autoservice.Controller.CarRepairGarage;
import com.oskin.autoservice.Controller.CarRepairMaster;
import com.oskin.autoservice.Controller.CarRepairOrders;
import com.oskin.configuration.Configuration.*;
import com.oskin.DI.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu {
    private static Builder builder = new Builder();
    private static Navigator navigator = new Navigator();
    private static MainMenu instance;

    @Inject
    CarRepairGarage carRepairGarage;
    @Inject
    CarRepairMaster carRepairMaster;
    @Inject
    CarRepairOrders carRepairOrders;
    @Inject
    CarRepairInput carRepairInput;
    @Inject
    CarRepairOutput carRepairOutput;

    private MainMenu(){}

    public static MainMenu getInstance(){
        if(instance == null){
            instance = new MainMenu();
        }
        return instance;
    }

    public void run(){
        carRepairGarage.loadGarage();
        carRepairMaster.loadMaster();
        carRepairOrders.loadOrder();

        builder.setTitle("Добавить данные");
        builder.addItem(1, "Добавить мастера",() -> carRepairInput.addMaster());
        builder.addItem(2, "Добавить место", () -> carRepairInput.addPlace());
        builder.addItem(3, "Добавить заказ", ()-> carRepairInput.addOrder());
        builder.addItem(4, "Добавить заказ мастеру", () -> carRepairInput.setOrderToMaster());
        navigator.addMenu(builder.build());
        builder.setTitle("Удалить данные");
        builder.addItem(1, "Удалить мастера", () -> carRepairInput.deleteMaster());
        builder.addItem(2, "Удалить место", () -> carRepairInput.deletePlace());
        builder.addItem(3, "Удалить заказ", () -> carRepairInput.deleteOrder());

        navigator.addMenu(builder.build());
        builder.setTitle("Изменить данные");
        builder.addItem(1, "Сместить время выполнения закзазов", () -> carRepairInput.offsetTimeOrder());
        builder.addItem(2, "Завершить заказ", () -> carRepairInput.completeOrder());
        builder.addItem(3, "Отменить заказ", () -> carRepairInput.cancelOrder());
        navigator.addMenu(builder.build());
        builder.setTitle("Получить данные");
        builder.addItem(1, "Свободные места", () -> carRepairOutput.getFreePlace());
        builder.addItem(2, "Список заказов", () -> carRepairOutput.getListOrders());
        builder.addItem(3, "Список мастеров", () -> carRepairOutput.getMasters());
        builder.addItem(4, "Текущие заказы", () -> carRepairOutput.getCurrentOrders());
        builder.addItem(5, "Заказы мастера", () -> carRepairOutput.getOrderByMaster());
        builder.addItem(6, "Мастера, выполняющие заказ", () -> carRepairOutput.getMastersByOrder());
        builder.addItem(7, "Заказы за определенный период", () -> carRepairOutput.getOrdersInTime());
        builder.addItem(8, "Количество свободных мест", () -> carRepairOutput.getCountInTime());
        builder.addItem(9, "Ближайшая свободна дата", () -> carRepairOutput.getNearestDate());
        navigator.addMenu(builder.build());
        builder.setTitle("Экспорт данных");
        builder.addItem(1, "Экспортировать места", ()-> carRepairGarage.exportGarage());
        builder.addItem(2, "Экспортировать мастеров", ()-> carRepairMaster.exportMaster());
        builder.addItem(3, "Экспортировать заказы", ()-> carRepairOrders.exportOrder());
        builder.addItem(4, "Сделать экспорт всех сущностей",
            () -> {
                carRepairGarage.exportGarage();
                carRepairMaster.exportMaster();
                carRepairOrders.exportOrder();
            });
        navigator.addMenu(builder.build());
        builder.setTitle("Импорт данных");
        builder.addItem(1, "Импорт мест", () -> carRepairGarage.importGarage());
        builder.addItem(2, "Импорт мастеров", () -> carRepairMaster.importMaster());
        builder.addItem(3, "Импорт заказов", () -> carRepairOrders.importOrder());
        builder.addItem(4, "Сделать импорт всех сущностей",
            () -> {
                carRepairGarage.importGarage();
                carRepairMaster.importMaster();
                carRepairOrders.importOrder();
            });
        navigator.addMenu(builder.build());
/*        builder.setTitle("Изменить конфигурацию");
        builder.addItem(1, "Начать изменение конфигурации", () -> Configuration.getInstance().toggle());
        navigator.addMenu(builder.build());*/

        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Введите действие: ");
            navigator.getNamesOfMenu();
            int x = 0;
            try{
                x = scanner.nextInt();
                scanner.nextLine();
                if(x == 0){
                    carRepairGarage.saveGarage();
                    carRepairMaster.saveMaster();
                    carRepairOrders.saveOrder();
                    return;
                }
            }
            catch (InputMismatchException e){
                System.err.println("\nНадо ввести только цифру!!!\n");
                scanner.nextLine();
            }
            navigator.navigate(x);
       }
    }
}
