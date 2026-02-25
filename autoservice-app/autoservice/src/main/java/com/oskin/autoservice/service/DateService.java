package com.oskin.autoservice.service;

import com.oskin.autoservice.dto.DateDto;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.TwoDateRequest;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.model.SortTypeMaster;
import com.oskin.autoservice.utils.MapperToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DateService {
    OrderService orderService;
    MasterService masterService;
    PlaceService placeService;
    MapperToDto mapperToDto;

    @Autowired
    public DateService(OrderService orderService, MasterService masterService, PlaceService placeService, MapperToDto mapperToDto) {
        this.orderService = orderService;
        this.masterService = masterService;
        this.placeService = placeService;
        this.mapperToDto = mapperToDto;
    }

    public int getCountFreePlaceInDate(LocalDateTime date) {
        int countPlace = getFreePlaceInDate(date).size();
        if (countPlace == 0) return 0;
        int countMaster = masterService.getListOfMasters(SortTypeMaster.ID).size();
        LocalDateTime start = date.toLocalDate().atStartOfDay();
        LocalDateTime finish = date.toLocalDate().atTime(23, 0);

        ArrayList<Order> ordersByTime = getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortTypeOrder.START);
        for (Order order : ordersByTime) {
            LocalDateTime timeStart = order.getTimeStart();
            LocalDateTime timeComplete = order.getTimeComplete();

            if (!timeStart.isAfter(date) && !timeComplete.isBefore(date)) {
                countMaster -= masterService.getMastersByOrder(order.getName()).size();
            }
        }
        return Math.min(countMaster, countPlace);
    }

    public DateDto getNearestDate() {
        LocalDateTime date = LocalDateTime.now();
        if (masterService.getListOfMasters(SortTypeMaster.ID).isEmpty() ||
                orderService.getListOfOrders(SortTypeOrder.ID).isEmpty()) {
            return null;
        }
        while (true) {
            int k = getCountFreePlaceInDate(date);
            if (k > 0) {
                return mapperToDto.mapToDateDto(date);
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

    public ArrayList<Place> getFreePlaceInDate(LocalDateTime date) {
        ArrayList<Place> newList = new ArrayList<>(placeService.getListOfPlace());
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime = getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortTypeOrder.START);
        for (Order order : ordersByTime) {
            if (!order.getTimeStart().isAfter(date) && !order.getTimeComplete().isBefore(date)) {
                newList.removeIf(place -> place.getId() == order.getPlace().getId());
            }
        }
        return newList;
    }

    public List<PlaceDto> getFreePlaceDto(LocalDateTime date) {
        List<PlaceDto> newList = new ArrayList<>(placeService.getListOfPlace().stream().map(mapperToDto::mapToPlaceDto).toList());
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime = getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortTypeOrder.START);
        for (Order order : ordersByTime) {
            if (!order.getTimeStart().isAfter(date) && !order.getTimeComplete().isBefore(date)) {
                newList.removeIf(place -> place.getId() == order.getPlace().getId());
            }
        }
        return newList;
    }

    public ArrayList<Order> getOrdersInTime(StatusOrder status, LocalDateTime startDate, LocalDateTime endDate, SortTypeOrder sortType) {
        ArrayList<Order> orders = orderService.getListOfOrders(sortType);
        ArrayList<Order> newList = new ArrayList<>();
        for (Order order : orders) {
            if (!order.getTimeStart().isAfter(endDate) && !order.getTimeComplete().isBefore(startDate)
                    && order.getStatus().equals(status)) {
                newList.add(order);
            }
        }
        return newList;
    }

    public List<OrderDto> getOrdersInTime(StatusOrder status, TwoDateRequest date, SortTypeOrder sortType) {
        ArrayList<Order> orders = orderService.getListOfOrders(sortType);
        List<OrderDto> newList = new ArrayList<>();
        for (Order order : orders) {
            if (!order.getTimeStart().isAfter(date.getEnd()) && !order.getTimeComplete().isBefore(date.getStart())
                    && order.getStatus().equals(status)) {
                newList.add(mapperToDto.mapToOrderDto(order));
            }
        }
        return newList;
    }
}
