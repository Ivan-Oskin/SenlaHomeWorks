package com.oskin.autoservice.View;

import com.oskin.autoservice.Controller.CarRepairGarage;
import com.oskin.autoservice.Controller.CarRepairMaster;
import com.oskin.autoservice.Controller.CarRepairOrders;
import com.oskin.configuration.Configuration;
import com.oskin.configuration.Configuration.*;

import java.util.InputMismatchException;
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
        Configuration configuration = new Configuration();
        configuration.configure(CarRepairGarage.getInstance());
        configuration.configure(CarRepairMaster.getInstance());
        configuration.configure(CarRepairOrders.getInstance());

        CarRepairGarage.getInstance().loadGarage();
        CarRepairMaster.getInstance().loadMaster();
        CarRepairOrders.getInstance().loadOrder();
        builder.setTitle("Добавить данные");
        builder.addItem(1, "Добавить мастера",() -> CarRepairInput.getInstance().addMaster());
        builder.addItem(2, "Добавить место", () -> CarRepairInput.getInstance().addPlace());
        builder.addItem(3, "Добавить заказ", ()-> CarRepairInput.getInstance().addOrder());
        builder.addItem(4, "Добавить заказ мастеру", () -> CarRepairInput.getInstance().setOrderToMaster());
        navigator.addMenu(builder.build());
        builder.setTitle("Удалить данные");
        builder.addItem(1, "Удалить мастера", () -> CarRepairInput.getInstance().deleteMaster());
        builder.addItem(2, "Удалить место", () -> CarRepairInput.getInstance().deletePlace());
        builder.addItem(3, "Удалить заказ", () -> CarRepairInput.getInstance().deleteOrder());

        navigator.addMenu(builder.build());
        builder.setTitle("Изменить данные");
        builder.addItem(1, "Сместить время выполнения закзазов", () -> CarRepairInput.getInstance().offsetTimeOrder());
        builder.addItem(2, "Завершить заказ", () -> CarRepairInput.getInstance().completeOrder());
        builder.addItem(3, "Отменить заказ", () -> CarRepairInput.getInstance().cancelOrder());
        navigator.addMenu(builder.build());
        builder.setTitle("Получить данные");
        builder.addItem(1, "Свободные места", () -> CarRepairOutput.getInstance().getFreePlace());
        builder.addItem(2, "Список заказов", () -> CarRepairOutput.getInstance().getListOrders());
        builder.addItem(3, "Список мастеров", () -> CarRepairOutput.getInstance().getMasters());
        builder.addItem(4, "Текущие заказы", () -> CarRepairOutput.getInstance().getCurrentOrders());
        builder.addItem(5, "Заказы мастера", () -> CarRepairOutput.getInstance().getOrderByMaster());
        builder.addItem(6, "Мастера, выполняющие заказ", () -> CarRepairOutput.getInstance().getMastersByOrder());
        builder.addItem(7, "Заказы за определенный период", () -> CarRepairOutput.getInstance().getOrdersInTime());
        builder.addItem(8, "Количество свободных мест", () -> CarRepairOutput.getInstance().getCountInTime());
        builder.addItem(9, "Ближайшая свободна дата", () -> CarRepairOutput.getInstance().getNearestDate());
        navigator.addMenu(builder.build());
        builder.setTitle("Экспорт данных");
        builder.addItem(1, "Экспортировать места", ()-> CarRepairGarage.getInstance().exportGarage());
        builder.addItem(2, "Экспортировать мастеров", ()-> CarRepairMaster.getInstance().exportMaster());
        builder.addItem(3, "Экспортировать заказы", ()-> CarRepairOrders.getInstance().exportOrder());
        builder.addItem(4, "Сделать экспорт всех сущностей",
            () -> {
                CarRepairGarage.getInstance().exportGarage();
                CarRepairMaster.getInstance().exportMaster();
                CarRepairOrders.getInstance().exportOrder();
            });
        navigator.addMenu(builder.build());
        builder.setTitle("Импорт данных");
        builder.addItem(1, "Импорт мест", () -> CarRepairGarage.getInstance().importGarage());
        builder.addItem(2, "Импорт мастеров", () -> CarRepairMaster.getInstance().importMaster());
        builder.addItem(3, "Импорт заказов", () -> CarRepairOrders.getInstance().importOrder());
        builder.addItem(4, "Сделать импорт всех сущностей",
            () -> {
                CarRepairGarage.getInstance().importGarage();
                CarRepairMaster.getInstance().importMaster();
                CarRepairOrders.getInstance().importOrder();
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
                    CarRepairGarage.getInstance().saveGarage();
                    CarRepairMaster.getInstance().saveMaster();
                    CarRepairOrders.getInstance().saveOrder();
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
