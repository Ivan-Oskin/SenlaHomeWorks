package com.oskin.task5.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CarRepairGarage {
    private static CarRepairGarage instance;

    private CarRepairGarage(){
    }

    public static CarRepairGarage getInstance(){
        if(instance == null)
            instance = new  CarRepairGarage();
        return instance;
    }


    private ArrayList<Place> garage = new ArrayList<>();

    public void addPlace(String name) {
        Place place = new Place(name);
        garage.add(place);
    }

    public boolean deletePlace(String name) {
        return CarRepair.delete(name, garage);
    }

    public ArrayList<Place> getListOfPlace() {
        return new ArrayList<>(garage);
    }
    public Place findPlace(String name) {
        int count = CarRepair.findByName(name, garage);
        if (count == -1) {
            return null;
        } else {
            return (Place) garage.get(count);
        }
    }



    public ArrayList<Place> getFreePlace(LocalDateTime date) {
        ArrayList<Place> newList = new ArrayList<Place>(garage);

        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime = CarRepairOrders.getInstance().getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortType.START);
        for (int i = 0; i < ordersByTime.size(); i++) {
            if (ordersByTime.get(i).getTimeStart().compareTo(date) <= 0 && ordersByTime.get(i).getTimeComplete().compareTo(date) >= 0) {
               CarRepair.delete(ordersByTime.get(i).getPlace().getName(), newList);
            }
        }
        return newList;
    }
}
