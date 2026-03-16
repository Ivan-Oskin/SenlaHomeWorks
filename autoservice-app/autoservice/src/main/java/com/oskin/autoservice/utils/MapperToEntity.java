package com.oskin.autoservice.utils;

import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.request.MasterRequest;
import com.oskin.autoservice.dto.request.OrderRequest;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.dto.request.UserRequest;
import com.oskin.autoservice.model.User;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.model.UserRole;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class MapperToEntity {

    public Place mapToPlaceEntity(PlaceRequest placeRequest) {
        return new Place(placeRequest.getName());
    }

    public Place mapToPlaceEntity(int id, PlaceRequest placeRequest) {
        return new Place(id, placeRequest.getName());
    }

    public Master mapToMasterEntity(int id, MasterRequest masterRequest) {
        return new Master(id, masterRequest.getName());
    }

    public Master mapToMasterEntity(MasterRequest masterRequest) {
        return new Master(masterRequest.getName());
    }

    public Order mapToOrderEntity(OrderRequest orderRequest, Place place) {
        LocalDateTime timeCreate = LocalDateTime.now();
        return new Order(
                orderRequest.getName(),
                orderRequest.getCost(),
                place,
                timeCreate,
                orderRequest.getTimeStart(),
                orderRequest.getTimeComplete()
        );
    }

    public OrderMaster mapToOrderMasterEntity(int id, OrderDto orderDto, MasterDto masterDto) {
        Place place = new Place(orderDto.getPlaceDto().getId(), orderDto.getPlaceDto().getName());
        StatusOrder statusOrder = StatusOrder.ACTIVE;
        switch (orderDto.getStatus()) {
            case "Cancel":
                statusOrder = StatusOrder.CANCEL;
            case "Close":
                statusOrder = StatusOrder.CLOSE;
        }
        Order order = new Order(
                orderDto.getId(),
                orderDto.getName(),
                orderDto.getCost(),
                place,
                orderDto.getTimeCreate(),
                orderDto.getTimeStart(),
                orderDto.getTimeComplete(),
                statusOrder
        );
        Master master = new Master(masterDto.getId(), masterDto.getName());
        return new OrderMaster(id, order, master);
    }

    public User mapToUserEntity(UserRequest userRequest) {
        return new User(userRequest.getLogin(), userRequest.getPassword(), UserRole.USER);
    }
}
