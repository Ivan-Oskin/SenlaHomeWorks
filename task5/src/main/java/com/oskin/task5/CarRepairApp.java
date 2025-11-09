package com.oskin.task5;

import java.util.Scanner;

public class CarRepairApp {
    private CarRepairView carRepairView = CarRepairView.getInstance();
    private CarRepairController carRepairController = CarRepairController.getInstance();
    private static CarRepairApp instance;
    private Scanner scanner = new Scanner(System.in);

    private CarRepairApp() {
    }

    public static CarRepairApp getInstance() {
        if (instance == null) {
            instance = new CarRepairApp();
        }
        return instance;
    }

    void run() {
        int number;
        while (true) {
            System.out.println("Введите действие: ");
            System.out.println("1.Добавить данные 2.Изменить данные 3.Удалить данные. 4.Получить данные 0.Выход");
            number = scanner.nextInt();
            switch (number) {
                case 1: {
                    int inNumber;
                    System.out.println("Введите номер");
                    System.out.println("1. Добавить мастера 2.Добавить место 3. Добавить заказ " +
                            "\n4. Добавить мастеру заказ 0.вернуться обратно");
                    while (true) {
                        inNumber = scanner.nextInt();
                        scanner.nextLine();
                        if (inNumber >= 0 && inNumber < 5) {
                            break;
                        }
                        System.out.println("Неправильный ввод");
                    }
                    switch (inNumber) {
                        case 0:
                            break;
                        case 1:
                            carRepairController.addMaster();
                            break;
                        case 2:
                            carRepairController.addPlace();
                            break;
                        case 3:
                            carRepairController.addOrder();
                            break;
                        case 4:
                            carRepairController.setOrderToMaster();
                            break;
                    }
                    break;
                }
                case 2: {
                    int inNumber;
                    System.out.println("Введите номер");
                    System.out.println("1. Сместить время заказа 2. Завершить заказ 3. Отменить заказ 0. Вернуться");
                    while (true){
                        inNumber = scanner.nextInt();
                        scanner.nextLine();
                        if(inNumber >= 0 && inNumber < 4) break;
                        System.out.println("Неправильный ввод");
                    }
                    switch (inNumber){
                        case 0: break;
                        case 1: carRepairController.offsetTimeOrder(); break;
                        case 2: carRepairController.completeOrder(); break;
                        case 3: carRepairController.cancelOrder(); break;
                    }
                    break;
                }
                case 3: {
                    int inNumber;
                    System.out.println("Введите номер");
                    System.out.println("1. Удалить мастера 2. Удалить Место 3. Удалить Заказ 0. Вернуться");
                    while (true) {
                        inNumber = scanner.nextInt();
                        scanner.nextLine();
                        if (inNumber >= 0 && inNumber < 4) {
                            break;
                        }
                        System.out.println("Неправильный ввод");
                    }
                    switch (inNumber) {
                        case 0:
                            break;
                        case 1:
                            carRepairController.deleteMaster();
                            break;
                        case 2:
                            carRepairController.deletePlace();
                            break;
                        case 3:
                            carRepairController.deleteOrder();
                            break;
                    }
                    break;
                }
                case 4: {
                    int inNumber;
                    System.out.println("Введите номер");
                    System.out.println("1. свободные места 2. Список заказов 3. Список мастеров\n" +
                            "4. Текущие заказы 5. Заказы мастера 6. Мастера, выполняющие заказ\n" +
                            "7. Заказы за определенный период 8. Количество свободных мест\n" +
                            "9. Ближайшая свободна дата 0. Вернуться");
                    while (true) {
                        inNumber = scanner.nextInt();
                        scanner.nextLine();
                        if (inNumber >= 0 && inNumber < 10) {
                            break;
                        }
                        System.out.println("Неправильный ввод");
                    }
                    switch (inNumber){
                        case 0: break;
                        case 1: carRepairView.getFreePlace(); break;
                        case 2: carRepairView.getListOrders(); break;
                        case 3: carRepairView.getMasters(); break;
                        case 4: carRepairView.getCurrentOrders(); break;
                        case 5: carRepairView.getOrderByMaster(); break;
                        case 6: carRepairView.getMastersByOrder(); break;
                        case 7: carRepairView.getOrdersInTime(); break;
                        case 8: carRepairView.getCountInTime(); break;
                        case 9: carRepairView.getNearestDate(); break;
                    }
                    break;
                }
                case 0: {
                    return;
                }
                default: {
                    System.out.println("Номер такого действия не существует");
                }
            }
        }
    }

    public static void main(String[] args) {
        CarRepairApp.getInstance().run();
    }
}
