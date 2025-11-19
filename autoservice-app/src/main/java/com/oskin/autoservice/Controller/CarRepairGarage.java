package com.oskin.autoservice.Controller;

import com.oskin.autoservice.Model.*;

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

    public void addPlace(int id, String name) {
        Place place = new Place(id, name);
        garage.add(place);
    }

    public boolean deletePlace(String name) {
        return CarRepair.delete(name, garage);
    }

    public ArrayList<Place> getListOfPlace() {
        return garage;
    }
    public Place findPlace(String name) {
        int count = CarRepair.findByName(name, garage);
        if (count == -1) {
            return null;
        } else {
            return garage.get(count);
        }
    }



    public ArrayList<Place> getFreePlace(LocalDateTime date) {
        ArrayList<Place> newList = new ArrayList<Place>(garage);
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime = CarRepairOrders.getInstance().getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortTypeOrder.START);
        for (int i = 0; i < ordersByTime.size(); i++) {
            if (ordersByTime.get(i).getTimeStart().compareTo(date) <= 0 && ordersByTime.get(i).getTimeComplete().compareTo(date) >= 0) {
               CarRepair.delete(ordersByTime.get(i).getPlace().getName(), newList);
            }
        }
        return newList;
    }

    public void exportGarage(){
        ArrayList<String> dataList = new ArrayList<>(garage.size()+1);
        dataList.add("ID,NAME\n");
        for(int i = 0; i<garage.size(); i++){
            int id = garage.get(i).getId();
            String name = garage.get(i).getName();
            dataList.add(id+","+name+"\n");
        }
        CarRepair.whereExport(dataList, FileName.GARAGE);
    }

    public void importGarage(){
        String nameFile = CarRepair.whereFromImport(FileName.GARAGE);
        if(nameFile.equals("???")){
            return;
        }
        ArrayList<ArrayList<String>> data = CarRepair.importData(nameFile);
        if(!data.isEmpty()){
            for(ArrayList<String> line : data){
                if(line.size() != 2){
                    System.out.println("Неправильная таблица данных");
                    return;
                }
                else {
                    try {
                        int id = Integer.parseInt(line.get(0));
                        String name = line.get(1);
                        int findPlace = CarRepair.findById(id, garage);
                        if(findPlace > -1){
                            if(!garage.get(findPlace).getName().equals(name)){
                                garage.set(findPlace, new Place(id, name));
                            }else{
                                continue;
                            }
                        }
                        else {
                            addPlace(id, name);
                        }
                    } catch (NumberFormatException e){
                        System.err.println("Неправильные данные");
                        return;
                    }
                }
            }
        }
    }
}
