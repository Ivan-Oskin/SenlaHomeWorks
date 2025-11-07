package com.oskin.task4.one;

import jdk.jfr.Enabled;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AdminCarRepairTest {

    public static <T extends Nameable> void printList(ArrayList<T> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ": " + list.get(i).getName());
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
        //3

        CarRepair admin = new CarRepair();
       /* admin.addMaster("Сергей");
        admin.deleteMaster("Сергей");
        admin.addPlace("Офис", 20);
        admin.deletePlace("Офис");
        LocalDateTime start_time1 = LocalDateTime.of(2025, 10, 12, 14, 0);
        LocalDateTime complete_time1 = LocalDateTime.of(2025, 10, 12, 18, 0);
        LocalDateTime start_time2 = LocalDateTime.of(2025, 10, 13, 15, 0);
        LocalDateTime complete_time2 = LocalDateTime.of(2025, 10, 13, 20, 0);
        admin.addOrder("Сменить колёса", "Андрей", start_time1, complete_time1);
        admin.addOrder("Покраска", "Артём", start_time2, complete_time2);
        admin.offsetDay("Покраска", 1);
        admin.offsetHour("Покраска", 2);
        admin.completeOrder("Сменить колёса");
        admin.deleteOrder("Сменить колёса");
        admin.cancelOrder("Покраска");
        */
        //4

        //добавление заказов, мест для проверки заданий

        admin.addPlace("Гаражное место 1");
        admin.addPlace("Гаражное место 2");
        admin.addPlace("Гаражное место 3");

        ArrayList<Place> listOfPlace = admin.getListOfPlace();

        LocalDateTime createTime1 = LocalDateTime.of(2025, 10, 10, 10, 0);
        LocalDateTime startTime1 = LocalDateTime.of(2025, 10, 13, 14, 0);
        LocalDateTime completeTime1 = LocalDateTime.of(2025, 10, 14, 18, 0);
        LocalDateTime createTime3 = LocalDateTime.of(2025, 10, 10, 10, 0);
        LocalDateTime startTime3 = LocalDateTime.of(2025, 10, 14, 14, 0);
        LocalDateTime completeTime3 = LocalDateTime.of(2025, 10, 14, 18, 0);
        LocalDateTime createTime2 = LocalDateTime.of(2025, 10, 13, 10, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2025, 10, 15, 14, 0);
        LocalDateTime completeTime2 = LocalDateTime.of(2025, 10, 16, 10, 0);


        admin.addOrder("покраска", 2000, listOfPlace.get(0), createTime1, startTime1, completeTime1);
        admin.addOrder("Поменять колёса", 1500, listOfPlace.get(1), createTime2, startTime2, completeTime2);
        admin.addOrder("Починить двигатель", 25000, listOfPlace.get(2), createTime3, startTime3, completeTime3);
        admin.cancelOrder("Починить двигатель");

        //список свободных мест в гараже
        LocalDateTime isFreeTime = LocalDateTime.of(2025, 10, 13, 12, 0);
        System.out.println("Свободные места: ");
        ArrayList<Place> listFreePlace = admin.getFreePlace(isFreeTime);
        printList(listFreePlace);

        //список заказов
        ArrayList<Order> listOrderCreate = admin.getListOfOrders(SortType.CREATE);
        ArrayList<Order> listOrderStart = admin.getListOfOrders(SortType.START);
        ArrayList<Order> listOrderComplete = admin.getListOfOrders(SortType.COMPLETE);
        ArrayList<Order> listOrderCost = admin.getListOfOrders(SortType.COST);
        System.out.println("Списки заказов: ");
        printList(listOrderCreate);
        printList(listOrderStart);
        printList(listOrderComplete);
        printList(listOrderCost);


        //список мастеров

        admin.addMaster("Сергей");
        admin.addMaster("Антон");
        admin.addMaster("Николай");
        admin.setOrderToMaster("Николай", listOrderCreate.get(0));
        admin.setOrderToMaster("Николай", listOrderCreate.get(1));
        admin.setOrderToMaster("Антон", listOrderCreate.get(0));

        ArrayList<Master> listMastersAlphabet = admin.getListOfMasters(true);

        ArrayList<Master> listMastersBusy = admin.getListOfMasters(false);
        System.out.println("Списки мастеров: ");
        printList(listMastersAlphabet);
        printList(listMastersBusy);


        //список аткивных заказов
        ArrayList<Order> listActiveCreate = admin.getListOfActiveOrders(SortType.CREATE);
        ArrayList<Order> listActiveComplete = admin.getListOfActiveOrders(SortType.COMPLETE);
        ArrayList<Order> listActiveCost = admin.getListOfActiveOrders(SortType.COST);
        System.out.println("Списки активных заказов: ");
        printList(listActiveCreate);
        printList(listActiveComplete);
        printList(listActiveCost);

        //Заказы выполняемый конкретным мастером
        System.out.println("Заказы мастера: ");
        ArrayList<Order> ordersByMaster = admin.getOrderByMaster("Николай");
        printList(ordersByMaster);

        //мастера выполняющие конкретный заказ
        ArrayList<Master> mastersByOrder = admin.getMastersByOrder("покраска");
        System.out.println("Мастера выполняющие заказ " + listOrderCreate.get(0).getName());
        printList(mastersByOrder);

        //Заказы за промежуток времени
        System.out.println("Заказы за промежуток времени: ");
        ArrayList<Order> listOrdersForTime1 = admin.getOrdersInTime(StatusOrder.CANCEL, createTime1, completeTime3, SortType.COST);
        ArrayList<Order> listOrdersForTime2 = admin.getOrdersInTime(StatusOrder.ACTIVE, createTime2, completeTime3, SortType.COMPLETE);
        printList(listOrdersForTime1);
        printList(listOrdersForTime2);

        //количество свободных мест на сервисе на любую дату
        int count = admin.getCountFreeTime(isFreeTime);

        System.out.println("Количество свободных мест: " + count);

        //Ближайшее свободнее время

        admin.addOrder("покраска", 2000, listOfPlace.get(0), createTime1, startTime1, completeTime1);
        admin.addOrder("Починка стекла", 2000, listOfPlace.get(1), createTime1, startTime1, completeTime1);
        admin.addOrder("Надуть колеса", 2000, listOfPlace.get(2), createTime1, startTime1, completeTime1);

        System.out.println("Время поиска:" + startTime1.getDayOfMonth() + "." + startTime1.getHour());
        System.out.println("Start: " + startTime1.getDayOfMonth() + "." + startTime1.getHour());
        System.out.println("End: " + completeTime1.getDayOfMonth() + "." + completeTime1.getHour());
        LocalDateTime foundFreeTime = admin.getNearestDate(startTime1);
        System.out.println("Ближайшее свободное время: " + foundFreeTime.getDayOfMonth() + "." + foundFreeTime.getHour());

    }
}
