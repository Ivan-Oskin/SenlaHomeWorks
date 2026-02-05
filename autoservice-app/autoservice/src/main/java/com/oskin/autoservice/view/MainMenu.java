package com.oskin.autoservice.view;
import com.oskin.autoservice.controller.CarRepairOrderMaster;
import com.oskin.autoservice.controller.CarRepairGarage;
import com.oskin.autoservice.controller.CarRepairMaster;
import com.oskin.autoservice.controller.CarRepairOrders;
import com.oskin.autoservice.controller.ImportDates;
import com.oskin.autoservice.repository.SessionHibernate;
import com.oskin.di.BuilderObject;
import com.oskin.di.DIContainer;
import com.oskin.di.InjectObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class MainMenu {
    DIContainer di = new DIContainer(new BuilderObject(), new InjectObject());
    private BuilderMenu builderMenu = di.getDependecy(BuilderMenu.class);
    private Navigator navigator = di.getDependecy(Navigator.class);
    CarRepairGarage carRepairGarage;
    CarRepairMaster carRepairMaster = di.getDependecy(CarRepairMaster.class);
    CarRepairOrders carRepairOrders = di.getDependecy(CarRepairOrders.class);
    CarRepairViewFunctions carRepairViewFunctions = di.getDependecy(CarRepairViewFunctions.class);
    ImportDates importDates = di.getDependecy(ImportDates.class);
    CarRepairOrderMaster carRepairOrderMaster = di.getDependecy(CarRepairOrderMaster.class);

    @Autowired
    public MainMenu(CarRepairGarage carRepairGarage) {
        this.carRepairGarage = carRepairGarage;
    }

    public MainMenu() {
    }

    public void run() {
        SessionHibernate.getSession();
        builderMenu.setTitle("Добавить данные");
        builderMenu.addItem(1, "Добавить мастера", () -> carRepairViewFunctions.addMaster());
        builderMenu.addItem(2, "Добавить место", () -> carRepairViewFunctions.addPlace());
        builderMenu.addItem(3, "Добавить заказ", () -> carRepairViewFunctions.addOrder());
        builderMenu.addItem(4, "Добавить заказ мастеру", () -> carRepairViewFunctions.setOrderToMaster());
        navigator.addMenu(builderMenu.build());
        builderMenu.setTitle("Удалить данные");
        builderMenu.addItem(1, "Удалить мастера", () -> carRepairViewFunctions.deleteMaster());
        builderMenu.addItem(2, "Удалить место", () -> carRepairViewFunctions.deletePlace());
        builderMenu.addItem(3, "Удалить заказ", () -> carRepairViewFunctions.deleteOrder());

        navigator.addMenu(builderMenu.build());
        builderMenu.setTitle("Изменить данные");
        builderMenu.addItem(1, "Сместить время выполнения закзазов", () -> carRepairViewFunctions.offsetTimeOrder());
        builderMenu.addItem(2, "Завершить заказ", () -> carRepairViewFunctions.completeOrder());
        builderMenu.addItem(3, "Отменить заказ", () -> carRepairViewFunctions.cancelOrder());
        navigator.addMenu(builderMenu.build());
        builderMenu.setTitle("Получить данные");
        builderMenu.addItem(1, "Свободные места", () -> carRepairViewFunctions.getFreePlace());
        builderMenu.addItem(2, "Список заказов", () -> carRepairViewFunctions.getListOrders());
        builderMenu.addItem(3, "Список мастеров", () -> carRepairViewFunctions.getMasters());
        builderMenu.addItem(4, "Текущие заказы", () -> carRepairViewFunctions.getCurrentOrders());
        builderMenu.addItem(5, "Заказы мастера", () -> carRepairViewFunctions.getOrderByMaster());
        builderMenu.addItem(6, "Мастера, выполняющие заказ", () -> carRepairViewFunctions.getMastersByOrder());
        builderMenu.addItem(7, "Заказы за определенный период", () -> carRepairViewFunctions.getOrdersInTime());
        builderMenu.addItem(8, "Количество свободных мест", () -> carRepairViewFunctions.getCountInTime());
        builderMenu.addItem(9, "Ближайшая свободна дата", () -> carRepairViewFunctions.getNearestDate());
        navigator.addMenu(builderMenu.build());
        builderMenu.setTitle("Экспорт данных");
        builderMenu.addItem(1, "Экспортировать места", () -> carRepairGarage.exportGarage());
        builderMenu.addItem(2, "Экспортировать мастеров", () -> carRepairMaster.exportMaster());
        builderMenu.addItem(3, "Экспортировать заказы", () -> carRepairOrders.exportOrder());
        builderMenu.addItem(4, "Экспортировать связи заказов и мастеров", () -> carRepairOrderMaster.exportOrderMaster());
        builderMenu.addItem(5, "Сделать экспорт всех сущностей",
                () -> {
                    carRepairGarage.exportGarage();
                    carRepairMaster.exportMaster();
                    carRepairOrders.exportOrder();
                    carRepairOrderMaster.exportOrderMaster();
                });
        navigator.addMenu(builderMenu.build());
        builderMenu.setTitle("Импорт данных");
        builderMenu.addItem(1, "Импорт мест", () -> importDates.importGarage());
        builderMenu.addItem(2, "Импорт мастеров", () -> importDates.importMaster());
        builderMenu.addItem(3, "Импорт заказов", () -> importDates.importOrder());
        builderMenu.addItem(4, "Импорт связи заказов и мастеров", () -> importDates.importOrderMaster());
        builderMenu.addItem(5, "Сделать импорт всех сущностей",
                () -> {
                    importDates.importGarage();
                    importDates.importMaster();
                    importDates.importOrder();
                    importDates.importOrderMaster();
                });
        navigator.addMenu(builderMenu.build());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите действие: ");
            navigator.getNamesOfMenu();
            int x = 0;
            try {
                x = scanner.nextInt();
                scanner.nextLine();
                if (x == 0) {
                    return;
                }
            } catch (InputMismatchException e) {
                System.err.println("\nНадо ввести только цифру!!!\n");
                scanner.nextLine();
            }
            navigator.navigate(x);
        }
    }
}
