package com.oskin.autoservice.utils;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.model.Place;
import org.springframework.stereotype.Component;



@Component
public class MapperToEntity {
    public Place mapToPlaceEntity(PlaceRequest placeRequest) {
        return new Place(placeRequest.getName());
    }
}
