package com.oskin.autoservice.controller;
import com.oskin.autoservice.repository.PlaceRepository;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.model.SortTypePlace;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.oskin.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CarRepairGarage {
    WorkWithFile workWithFile;
    Config config;
    PlaceRepository placeRepository;
    CarRepairOrders carRepairOrders;

    Logger logger = LoggerFactory.getLogger(CarRepairGarage.class);

    @Autowired
    public CarRepairGarage(WorkWithFile workWithFile, Config config, PlaceRepository placeRepository, CarRepairOrders carRepairOrders) {
        this.workWithFile = workWithFile;
        this.config = config;
        this.placeRepository = placeRepository;
        this.carRepairOrders = carRepairOrders;
    }

    public void addPlace(int id, String name) {
        Place place = new Place(id, name);
        placeRepository.create(place);
    }

    public boolean deletePlace(String name) {
        return placeRepository.delete(name);
    }

    public Place findPlace(int id) {
        return placeRepository.find(id);
    }

    public void updatePlace(Place place) {
        placeRepository.update(place);
    }

    public ArrayList<Place> getListOfPlace() {
        return placeRepository.findAll(SortTypePlace.ID);
    }
    public Place findPlace(String name) {
        return placeRepository.find(name);
    }
    public ArrayList<Place> getFreePlace(LocalDateTime date) {
        ArrayList<Place> newList = new ArrayList<>(getListOfPlace());
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime = carRepairOrders.getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortTypeOrder.START);
        for (Order order : ordersByTime) {
            if (order.getTimeStart().compareTo(date) <= 0 && order.getTimeComplete().compareTo(date) >= 0) {
               newList.removeIf(place -> place.getId() == order.getPlace().getId());
            }
        }
        return newList;
    }

    public void exportGarage() {
        logger.info("Start export place");
        ArrayList<Place> places = placeRepository.findAll(SortTypePlace.ID);
        int size = places.size();
        ArrayList<String> dataList = new ArrayList<>(size + 1);
        dataList.add("ID,NAME\n");
        for (int i = 0; i < size; i++) {
            int id = places.get(i).getId();
            String name = places.get(i).getName();
            dataList.add(id + "," + name + "\n");
        }
        workWithFile.whereExport(dataList, config.getStandardFileCsvGarage());
    }
}
