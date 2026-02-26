package com.oskin.autoservice.controller;

import com.oskin.autoservice.dto.DateDto;
import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.OrderMasterDto;
import com.oskin.autoservice.dto.request.OrderMasterRequest;
import com.oskin.autoservice.service.DateService;
import com.oskin.autoservice.service.MasterService;
import com.oskin.autoservice.service.OrderMasterService;
import com.oskin.autoservice.service.OrderService;
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
@RequestMapping("/autoservice/order_master")
public class OrderMasterController {
    public final OrderMasterService orderMasterService;
    public final OrderService orderService;
    public final MasterService masterService;
    public final DateService dateService;

    @Autowired
    public OrderMasterController(OrderMasterService orderMasterService, OrderService orderService, MasterService masterService, DateService dateService) {
        this.orderMasterService = orderMasterService;
        this.orderService = orderService;
        this.masterService = masterService;
        this.dateService = dateService;
    }

    @GetMapping
    public List<OrderMasterDto> findAll() {
        return orderMasterService.getListOfOrderMasterDto();
    }

    @GetMapping("/{id}")
    public OrderMasterDto findById(@PathVariable("id") int id) {
        return orderMasterService.findOrderMaster(id);
    }

    @GetMapping("/master/{masterId}")
    public List<OrderMasterDto> findByMaster(@PathVariable("masterId") int id) {
        return orderMasterService.getOrderMasterDtoByMaster(id);
    }

    @GetMapping("/order/{orderId}")
    public List<OrderMasterDto> findByOrder(@PathVariable("orderId") int id) {
        return orderMasterService.getOrderMasterDtoByOrder(id);
    }

    @GetMapping("/nearest_date")
    public DateDto getNearestDate() {
        return dateService.getNearestDate();
    }

    @PostMapping
    public void create(@RequestBody OrderMasterRequest orderMasterRequest) {
        orderMasterService.addOrderMaster(orderMasterRequest.getMasterId(), orderMasterRequest.getOrderId());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") int id, @RequestBody OrderMasterRequest orderMasterRequest) {
        OrderDto orderDto = orderService.findOrder(orderMasterRequest.getOrderId());
        MasterDto masterDto = masterService.findMaster(orderMasterRequest.getMasterId());
        orderMasterService.updateOrderMaster(id, orderDto, masterDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        orderMasterService.delete(id);
    }
}
