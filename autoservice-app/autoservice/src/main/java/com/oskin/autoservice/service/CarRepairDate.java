package com.oskin.autoservice.service;

import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.SortTypeMaster;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.model.StatusOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CarRepairDate {
    CarRepairOrders carRepairOrders;
    CarRepairMaster carRepairMaster;
    CarRepairGarage carRepairGarage;

    @Autowired
    public CarRepairDate(CarRepairOrders carRepairOrders, CarRepairMaster carRepairMaster, CarRepairGarage carRepairGarage) {
        this.carRepairOrders = carRepairOrders;
        this.carRepairMaster = carRepairMaster;
        this.carRepairGarage = carRepairGarage;
    }

    //Количество свободных мест на любую дату
    public int getCountFreeTime(LocalDateTime date) {
        int countPlace = carRepairGarage.getFreePlace(date).size();
        if (countPlace == 0) return 0;
        int countMaster = carRepairMaster.getListOfMasters(SortTypeMaster.ID).size();
        LocalDateTime start = date.toLocalDate().atStartOfDay();
        LocalDateTime finish = date.toLocalDate().atTime(23, 0);

        ArrayList<Order> ordersByTime = carRepairOrders.getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortTypeOrder.START);
        for (Order order : ordersByTime) {
            LocalDateTime timeStart = order.getTimeStart();
            LocalDateTime timeComplete = order.getTimeComplete();

            if (!timeStart.isAfter(date) && !timeComplete.isBefore(date)) {
                countMaster -= carRepairMaster.getMastersByOrder(order.getName()).size();
            }
        }
        return Math.min(countMaster, countPlace);
    }

    public LocalDateTime getNearestDate(LocalDateTime fromDate) {
        LocalDateTime date = fromDate;
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
