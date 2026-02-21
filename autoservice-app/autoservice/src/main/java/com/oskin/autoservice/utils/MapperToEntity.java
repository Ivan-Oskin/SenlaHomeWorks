package com.oskin.autoservice.utils;

import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.OrderMasterDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.model.*;

public class MapperToEntity {
    public Place mapToPlaceEntity(PlaceDto placeDto){
        return new Place(placeDto.getId(), placeDto.getName());
    }
    public Master mapToMasterEntity(MasterDto masterDto){
        return new Master(masterDto.getId(), masterDto.getName());
    }
    public Order mapToOrderEntity(OrderDto orderDto){
        Place place = mapToPlaceEntity(orderDto.getPlaceDto());
        StatusOrder statusOrder = StatusOrder.ACTIVE;
        switch (orderDto.getStatus()){
            case "Cancel": statusOrder = StatusOrder.CANCEL;
            case "Close" : statusOrder = StatusOrder.CLOSE;
        }
        return new Order(orderDto.getId(), orderDto.getName(), orderDto.getCost(), place,
                orderDto.getTimeCreate(), orderDto.getTimeStart(), orderDto.getTimeComplete(), statusOrder);
    }
    public OrderMaster mapToOrderMasterEntity(OrderMasterDto orderMasterDto){
        Order order = mapToOrderEntity(orderMasterDto.getOrderDto());
        Master master = mapToMasterEntity(orderMasterDto.getMasterDto());
        return new OrderMaster(orderMasterDto.getId(), order, master);
    }
}
