package com.oskin.autoservice.controller;

import com.oskin.autoservice.dto.DateDto;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.TwoDateRequest;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.service.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/autoservice")
public class DateController {
    private final DateService dateService;

    @Autowired
    public DateController(DateService dateService) {
        this.dateService = dateService;
    }

    @PostMapping("/places/free")
    public List<PlaceDto> getFreePlace(@RequestBody DateDto date) {
        return dateService.getFreePlaceDto(date.getDate());
    }

    @PostMapping("/places/count_free")
    public int getCountFreePlace(@RequestBody DateDto date) {
        return dateService.getCountFreePlaceInDate(date.getDate());
    }

    @GetMapping("/nearest_date")
    public DateDto getNearestDate() {
        return dateService.getNearestDate();
    }

    @PostMapping("/orders/in_time/{status}/{sortType}")
    public List<OrderDto> getOrderInTimeByCreate(@PathVariable("status") String status, @PathVariable("sortType") String sortType, @RequestBody TwoDateRequest date) {
        StatusOrder statusOrder = StatusOrder.ACTIVE;
        SortTypeOrder sortTypeOrder = SortTypeOrder.CREATE;
        switch (status) {
            case "cancel":
                statusOrder = StatusOrder.CANCEL;
            case "close":
                statusOrder = StatusOrder.CLOSE;
        }
        switch (sortType) {
            case "sort_by_time_start":
                sortTypeOrder = SortTypeOrder.START;
            case "sort_by_cost":
                sortTypeOrder = SortTypeOrder.COST;
        }
        return dateService.getOrdersInTime(statusOrder, date, sortTypeOrder);
    }
}
