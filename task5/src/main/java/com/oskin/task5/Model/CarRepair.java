package com.oskin.task5.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CarRepair {

    //Метод для поиска

    public static  <T extends Nameable> int findByName(String name, ArrayList<T> list) {
        int number = -1;
        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getName())) {
                number = i;
                break;
            }
        }
        return number;
    }


    // Метод для удаления

    public static  <T extends Nameable> boolean delete(String name, ArrayList<T> list) {
        int i = findByName(name, list);
        if (i == -1) {
            return false;
        } else {
            list.remove(i);
            return true;
        }
    }

    //Количество свободных мест на любую дату
    public static int getCountFreeTime(LocalDateTime date) {
        int countPlace = CarRepairGarage.getInstance().getFreePlace(date).size();
        if (countPlace == 0) return 0;
        int countMaster = CarRepairMaster.getInstance().getListOfMasters(true).size();
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime =CarRepairOrders.getInstance().getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortType.START);
        for (int i = 0; i < ordersByTime.size(); i++) {
            if (ordersByTime.get(i).getTimeStart().compareTo(date) <= 0 && ordersByTime.get(i).getTimeComplete().compareTo(date) >= 0) {
                countMaster -=CarRepairMaster.getInstance().getMastersByOrder(ordersByTime.get(i).getName()).size();
            }
        }
        if (countPlace > countMaster) {
            return countMaster;
        } else {
            return countPlace;
        }
    }

    public static LocalDateTime getNearestDate(LocalDateTime fromDate) {
        LocalDateTime date = LocalDateTime.from(fromDate);
        while (true) {
            int k = getCountFreeTime(date);
            if (k > 0) {
                return date;
            } else {
                if (date.getDayOfWeek().getValue() >= 6) {
                    date = date.plusDays(8 - date.getDayOfWeek().getValue());
                    date = date.withHour(10);
                }
                if (date.getHour() < 10) {
                    date = date.withHour(10);
                } else if (date.getHour() >= 18) {
                    date = date.plusDays(1);
                    date = date.withHour(10);
                } else {
                    date = date.plusHours(1);
                }
            }

        }
    }
}



