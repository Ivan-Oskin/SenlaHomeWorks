package com.oskin.autoservice.utils;
import com.oskin.autoservice.dto.request.MasterRequest;
import com.oskin.autoservice.dto.request.OrderRequest;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.Place;
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
}
