package com.oskin.autoservice.service;

import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.OffsetRequest;
import com.oskin.autoservice.dto.request.OrderRequest;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.utils.MapperToDto;
import com.oskin.autoservice.utils.MapperToEntity;
import com.oskin.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    OrderRepository orderRepository;
    MasterRepository masterRepository;
    OrderMasterRepository orderMasterRepository;
    OrderMasterService orderMasterService;
    MapperToDto mapperToDto;
    MapperToEntity mapperToEntity;
    Config config;

    @Autowired
    public OrderService(OrderRepository orderRepository, MasterRepository masterRepository, OrderMasterRepository orderMasterRepository,
                        OrderMasterService orderMasterService, Config config, MapperToDto mapperToDto, MapperToEntity mapperToEntity) {
        this.orderRepository = orderRepository;
        this.masterRepository = masterRepository;
        this.orderMasterRepository = orderMasterRepository;
        this.orderMasterService = orderMasterService;
        this.config = config;
        this.mapperToDto = mapperToDto;
        this.mapperToEntity = mapperToEntity;
    }

    @Transactional
    public void addOrder(OrderRequest orderRequest, PlaceDto placeDto) {
        Place place = new Place(placeDto.getId(), placeDto.getName());
        Order order = mapperToEntity.mapToOrderEntity(orderRequest, place);
        orderRepository.create(order);
    }

    @Transactional
    public void deleteOrder(int id) {
        Order order = orderRepository.find(id);
        if (order != null) {
            orderMasterRepository.deleteByOrder(id);
            orderRepository.delete(id);
        }
    }

    public OrderDto findOrder(int id) {
        return mapperToDto.mapToOrderDto(orderRepository.find(id));
    }

    @Transactional
    public void updateOrder(int id, OrderRequest orderRequest, PlaceDto placeDto) {
        OrderDto orderDto = findOrder(id);
        Place place = new Place(placeDto.getId(), placeDto.getName());
        Order order = new Order(
                id,
                orderRequest.getName(),
                orderRequest.getCost(),
                place,
                orderDto.getTimeCreate(),
                orderRequest.getTimeStart(),
                orderRequest.getTimeComplete()
        );

        orderRepository.update(order);
    }

    @Transactional
    public void closeOrder(int id) {
        orderRepository.changeStatusInDb(id, StatusOrder.CLOSE);
    }

    @Transactional
    public void cancelOrder(int id) {
        orderRepository.changeStatusInDb(id, StatusOrder.CANCEL);
    }

    @Transactional
    public void activateOrder(int id) {
        orderRepository.changeStatusInDb(id, StatusOrder.ACTIVE);
    }

    @Transactional
    public void offset(int id, OffsetRequest offsetRequest) {
        Order order = orderRepository.find(id);
        if (order != null) {
            LocalDateTime startTime = order.getTimeStart();
            LocalDateTime completeTime = order.getTimeComplete();
            LocalDateTime ChangeStartTime = startTime.plusDays(offsetRequest.getDay());
            LocalDateTime ChangeCompleteTime = completeTime.plusDays(offsetRequest.getDay());
            startTime = ChangeStartTime.plusHours(offsetRequest.getHour());
            completeTime = ChangeCompleteTime.plusHours(offsetRequest.getHour());
            orderRepository.offsetInDb(id, startTime, completeTime);
        }
    }

    public ArrayList<Order> getListOfOrders(SortTypeOrder sortType) {
        return orderRepository.findAll(sortType);
    }

    public List<OrderDto> getListOfOrdersDto(SortTypeOrder sortType) {
        return orderRepository.findAll(sortType).stream().map(order -> mapperToDto.mapToOrderDto(order)).toList();
    }

    public List<OrderDto> getListOfActiveOrders(SortTypeOrder sortType) {
        return orderRepository.findAll(sortType)
                .stream()
                .filter(order -> order.getStatus() == StatusOrder.ACTIVE)
                .map(mapperToDto::mapToOrderDto)
                .toList();
    }
}


