package com.oskin.autoservice.utils;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.OrderMasterDto;
import com.oskin.autoservice.dto.DateDto;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.autoservice.model.Place;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class MapperToDto {
    public PlaceDto mapToPlaceDto(Place placeEntity) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setId(placeEntity.getId());
        placeDto.setName(placeEntity.getName());
        return placeDto;
    }
    public MasterDto mapToMasterDto(Master masterEntity) {
        MasterDto masterDto = new MasterDto();
        masterDto.setId(masterEntity.getId());
        masterDto.setName(masterEntity.getName());
        return masterDto;
    }
    public OrderDto mapToOrderDto(Order orderEntity) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(orderEntity.getId());
        orderDto.setCost(orderEntity.getCost());
        orderDto.setName(orderEntity.getName());
        orderDto.setStatus(orderEntity.getStatus());
        orderDto.setTimeStart(orderEntity.getTimeStart());
        orderDto.setTimeCreate(orderEntity.getTimeCreate());
        orderDto.setTimeComplete(orderEntity.getTimeComplete());
        orderDto.setPlaceDto(mapToPlaceDto(orderEntity.getPlace()));
        return orderDto;
    }
    public OrderMasterDto mapToOrderMasterDto(OrderMaster orderMasterEntity) {
        OrderMasterDto orderMasterDto = new OrderMasterDto();
        orderMasterDto.setId(orderMasterEntity.getId());
        orderMasterDto.setMasterDto(mapToMasterDto(orderMasterEntity.getMaster()));
        orderMasterDto.setOrderDto(mapToOrderDto(orderMasterEntity.getOrder()));
        return orderMasterDto;
    }

    public DateDto mapToDateDto(LocalDateTime localDateTime) {
        return new DateDto(localDateTime);
    }
}
