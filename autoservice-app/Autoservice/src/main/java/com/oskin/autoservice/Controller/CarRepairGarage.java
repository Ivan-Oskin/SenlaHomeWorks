package com.oskin.autoservice.Controller;
import com.oskin.Annotations.Inject;
import com.oskin.autoservice.repository.PlaceRepository;
import com.oskin.autoservice.Model.Place;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.StatusOrder;
import com.oskin.autoservice.Model.SortTypeOrder;
import com.oskin.autoservice.Model.SortTypePlace;
import com.oskin.Annotations.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.oskin.config.Config;

@Singleton
public final class CarRepairGarage {
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;
    @Inject
    PlaceRepository placeRepository;
    @Inject
    CarRepairOrders carRepairOrders;
    private static CarRepairGarage instance;
    private CarRepairGarage() {
    }

    public static CarRepairGarage getInstance() {
        if (instance == null)
            instance = new  CarRepairGarage();
        return instance;
    }

    public void addPlace(int id, String name) {
        Place place = new Place(id, name);
        placeRepository.create(place);
    }

    public boolean deletePlace(String name) {
        return placeRepository.delete(name);
    }

    public ArrayList<Place> getListOfPlace() {
        return placeRepository.findAll(SortTypePlace.ID);
    }
    public Place findPlace(String name) {
        return placeRepository.find(name);
    }
    public ArrayList<Place> getFreePlace(LocalDateTime date) {
        ArrayList<Place> newList = new ArrayList<Place>(getListOfPlace());
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
