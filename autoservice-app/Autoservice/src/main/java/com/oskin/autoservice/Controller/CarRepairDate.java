package com.oskin.autoservice.controller;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.SortTypeMaster;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.model.StatusOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CarRepairDate {
    @Inject
    CarRepairOrders carRepairOrders;
    @Inject
    CarRepairMaster carRepairMaster;
    @Inject
    CarRepairGarage carRepairGarage;

    //Количество свободных мест на любую дату
    public int getCountFreeTime(LocalDateTime date) {
        int countPlace = carRepairGarage.getFreePlace(date).size();
        if (countPlace == 0) return 0;
        int countMaster = carRepairMaster.getListOfMasters(SortTypeMaster.ID).size();
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime = carRepairOrders.getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortTypeOrder.START);
        for (int i = 0; i < ordersByTime.size(); i++) {
            if (ordersByTime.get(i).getTimeStart().compareTo(date) <= 0 && ordersByTime.get(i).getTimeComplete().compareTo(date) >= 0) {
                countMaster -= CarRepairMaster.getInstance().getMastersByOrder(ordersByTime.get(i).getName()).size();
            }
        }
        if (countPlace > countMaster) {
            return countMaster;
        } else {
            return countPlace;
        }
    }

    public LocalDateTime getNearestDate(LocalDateTime fromDate) {
        LocalDateTime date = LocalDateTime.from(fromDate);
        if (carRepairMaster.getListOfMasters(SortTypeMaster.ID).isEmpty() ||
                carRepairOrders.getListOfOrders(SortTypeOrder.ID).isEmpty()) {
            return null;
        }
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
