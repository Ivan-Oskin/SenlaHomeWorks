package com.oskin.autoservice.controller;

import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.OffsetRequest;
import com.oskin.autoservice.dto.request.OrderRequest;
import com.oskin.autoservice.dto.request.TwoDateRequest;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.service.DateService;
import com.oskin.autoservice.service.OrderService;
import com.oskin.autoservice.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/autoservice/orders")
public class OrderController {
    private final OrderService orderService;
    private final PlaceService placeService;
    private final DateService dateService;

    @Autowired
    public OrderController(OrderService orderService, PlaceService placeService, DateService dateService) {
        this.orderService = orderService;
        this.placeService = placeService;
        this.dateService = dateService;
    }

    @GetMapping
    public List<OrderDto> findAll() {
        return orderService.getListOfOrdersDto(SortTypeOrder.ID);
    }

    @GetMapping("/sort_by_time_create")
    public List<OrderDto> findAllByCreate() {
        return orderService.getListOfOrdersDto(SortTypeOrder.CREATE);
    }

    @GetMapping("/sort_by_time_start")
    public List<OrderDto> findAllByStart() {
        return orderService.getListOfOrdersDto(SortTypeOrder.START);
    }

    @GetMapping("/sort_by_time_complete")
    public List<OrderDto> findAllByComplete() {
        return orderService.getListOfOrdersDto(SortTypeOrder.COMPLETE);
    }

    @GetMapping("/sort_by_cost")
    public List<OrderDto> findAllByCost() {
        return orderService.getListOfOrdersDto(SortTypeOrder.COST);
    }

    @GetMapping("/active/sort_by_time_create")
    public List<OrderDto> findActiveByCreate() {
        return orderService.getListOfActiveOrders(SortTypeOrder.CREATE);
    }

    @GetMapping("/active/sort_by_time_complete")
    public List<OrderDto> findActiveByComplete() {
        return orderService.getListOfActiveOrders(SortTypeOrder.COMPLETE);
    }

    @GetMapping("/active/sort_by_cost")
    public List<OrderDto> findActiveByCost() {
        return orderService.getListOfActiveOrders(SortTypeOrder.COST);
    }

    @GetMapping("/{id}")
    public OrderDto findById(@PathVariable("id") int id) {
        return orderService.findOrder(id);
    }

    @GetMapping("/in_time/{status}/{sortType}")
    public List<OrderDto> getOrderInTime(@PathVariable("status") String status, @PathVariable("sortType") String sortType, @RequestBody TwoDateRequest date) {
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

    @PostMapping
    public void save(@RequestBody OrderRequest orderRequest) {
        PlaceDto place = placeService.findPlace(orderRequest.getPlaceId());
        if (place != null) {
            orderService.addOrder(orderRequest, place);
        }
    }

    @PostMapping("/offset/{id}")
    public void offset(@PathVariable("id") int id, @RequestBody OffsetRequest offsetRequest) {
        orderService.offset(id, offsetRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") int id, @RequestBody OrderRequest orderRequest) {
        PlaceDto placeDto = placeService.findPlace(orderRequest.getPlaceId());
        if (placeDto != null) {
            orderService.updateOrder(id, orderRequest, placeDto);
        }
    }

    @PutMapping("/close/{id}")
    public void close(@PathVariable("id") int id) {
        orderService.closeOrder(id);
    }

    @PutMapping("/cancel/{id}")
    public void cancel(@PathVariable("id") int id) {
        orderService.cancelOrder(id);
    }

    @PutMapping("/activate/{id}")
    public void activate(@PathVariable("id") int id) {
        orderService.activateOrder(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        orderService.deleteOrder(id);
    }
}
