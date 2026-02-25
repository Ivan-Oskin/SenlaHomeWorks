package com.oskin.autoservice.service;

import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.repository.PlaceRepository;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.model.SortTypePlace;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.oskin.autoservice.utils.MapperToDto;
import com.oskin.autoservice.utils.MapperToEntity;
import com.oskin.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PlaceService {
    Config config;
    PlaceRepository placeRepository;
    OrderService orderService;
    MapperToDto mapperToDto;
    MapperToEntity mapperToEntity;

    @Autowired
    public PlaceService(Config config, PlaceRepository placeRepository, OrderService orderService, MapperToDto mapperToDto, MapperToEntity mapperToEntity) {
        this.config = config;
        this.placeRepository = placeRepository;
        this.orderService = orderService;
        this.mapperToDto = mapperToDto;
        this.mapperToEntity = mapperToEntity;
    }

    @Transactional
    public void addPlace(PlaceRequest placeRequest) {
        Place place = mapperToEntity.mapToPlaceEntity(placeRequest);
        placeRepository.create(place);
    }

    @Transactional
    public boolean deletePlace(String name) {
        return placeRepository.delete(name);
    }
    @Transactional
    public boolean deletePlace(int id) {
        return placeRepository.delete(id);
    }
    public PlaceDto findPlace(int id) {
        return mapperToDto.mapToPlaceDto(placeRepository.find(id));
    }
    @Transactional
    public void updatePlace(int id, PlaceRequest placeRequest) {
        Place place = new Place(id, placeRequest.getName());
        placeRepository.update(place);
    }

    public ArrayList<Place> getListOfPlace() {
        return placeRepository.findAll(SortTypePlace.ID);
    }
    public List<PlaceDto> getListOfPlaceDto() {
        return placeRepository.findAll(SortTypePlace.ID).stream().map(place ->
                mapperToDto.mapToPlaceDto(place)).toList();
    }

    public Place findPlace(String name) {
        return placeRepository.find(name);
    }
    public ArrayList<Place> getFreePlace(LocalDateTime date) {
        ArrayList<Place> newList = new ArrayList<>(getListOfPlace());
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime = orderService.getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortTypeOrder.START);
        for (Order order : ordersByTime) {
            if (!order.getTimeStart().isAfter(date) && !order.getTimeComplete().isBefore(date)) {
                newList.removeIf(place -> place.getId() == order.getPlace().getId());
            }
        }
        return newList;
    }
}
