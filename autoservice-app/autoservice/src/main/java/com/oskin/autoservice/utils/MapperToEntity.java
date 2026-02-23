package com.oskin.autoservice.utils;

import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.OrderMasterDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.model.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


@Component
public class MapperToEntity {
    public Place mapToPlaceEntity(PlaceRequest placeRequest){
        return new Place(placeRequest.getName());
    }
}
