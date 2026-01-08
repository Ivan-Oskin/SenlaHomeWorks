package com.oskin.autoservice.Controller;
import com.oskin.autoservice.DAO.PlaceBD;
import com.oskin.autoservice.Model.*;
import com.oskin.Annotations.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.oskin.config.Config;

@Singleton
public class CarRepairGarage {
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;
    @Inject
    PlaceBD placeBD;
    @Inject
    CarRepairOrders carRepairOrders;
    @Inject
    CarRepairFunctions carRepairFunctions;
    private static CarRepairGarage instance;
    private CarRepairGarage(){
    }

    public static CarRepairGarage getInstance(){
        if(instance == null)
            instance = new  CarRepairGarage();
        return instance;
    }

    public void addPlace(int id, String name) {
        Place place = new Place(id, name);
        placeBD.addPlaceInDB(place);
    }

    public boolean deletePlace(String name) {
        return placeBD.deletePlaceInDB(name);
    }

    public ArrayList<Place> getListOfPlace() {
        return placeBD.selectPlace();
    }
    public Place findPlace(String name) {
        return placeBD.findPlaceInDb(name);

    }
    public ArrayList<Place> getFreePlace(LocalDateTime date) {
        ArrayList<Place> newList = new ArrayList<Place>(getListOfPlace());
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime = carRepairOrders.getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortTypeOrder.START);
        for (int i = 0; i < ordersByTime.size(); i++) {
            if (ordersByTime.get(i).getTimeStart().compareTo(date) <= 0 && ordersByTime.get(i).getTimeComplete().compareTo(date) >= 0) {
               carRepairFunctions.delete(ordersByTime.get(i).getPlace().getName(), newList);
            }
        }
        return newList;
    }

    public void exportGarage(){
        ArrayList<Place> places = placeBD.selectPlace();
        int size = places.size();
        ArrayList<String> dataList = new ArrayList<>(size+1);
        dataList.add("ID,NAME\n");
        for(int i = 0; i<size; i++){
            int id = places.get(i).getId();
            String name = places.get(i).getName();
            dataList.add(id+","+name+"\n");
        }
        workWithFile.whereExport(dataList, config.getStandartFileCsvGarage());
    }
}
