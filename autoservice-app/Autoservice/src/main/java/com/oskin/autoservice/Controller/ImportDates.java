package com.oskin.autoservice.Controller;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.DAO.*;
import com.oskin.autoservice.Model.*;
import com.oskin.config.Config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ImportDates {

    @Inject
    CarRepairOrders carRepairOrders;
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;
    @Inject
    CarRepair carRepair;
    @Inject
    OrderDB orderDB;
    @Inject
    CarRepairGarage carRepairGarage;
    @Inject
    CarRepairMaster carRepairMaster;
    @Inject
    MasterDB masterDB;
    @Inject
    PlaceBD placeBD;
    @Inject
    OrdersByMasterDb ordersByMasterDb;

    public boolean DeleteAllAgree(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Вы уверены, это действие приведет к перезаписи данных при совпадении.\nY/N");
            String line =  scanner.nextLine();
            if(line.toLowerCase().equals("y")){
                return true;
            }
            else if(line.toLowerCase().equals("n")){
                return false;
            }
        }
    }

    public void importOrder(){
        boolean next = DeleteAllAgree();
        if(!next) return;
        ArrayList<Order> orders = carRepairOrders.getListOfOrders(SortTypeOrder.ID);
        String nameFile = workWithFile.whereFromImport(config.getStandartFileCsvOrders());
        if(nameFile.equals("???")){
            return;
        }
        ArrayList<ArrayList<String>> data = workWithFile.importData(nameFile);
        if(!data.isEmpty()){
            for(ArrayList<String> line : data){
                if(line.size() != 8){
                    System.out.println("Неправильная таблица данных");
                    return;
                }
                else {
                    int id;
                    String name;
                    int cost;
                    StatusOrder status = null;
                    LocalDateTime create;
                    LocalDateTime start;
                    LocalDateTime complete;
                    Place place;
                    try {
                        id = Integer.parseInt(line.get(0));
                        name = line.get(1);
                        cost = Integer.parseInt(line.get(2));
                        String statusString = line.get(3);
                        for(StatusOrder statusOrder : StatusOrder.values()){
                            if(statusOrder.getSTATUS().equals(statusString)){
                                status = statusOrder;
                                break;
                            }
                        }
                        if(status == null){
                            System.err.println("Неправильные данные в заказе "+name);
                            continue;
                        }
                        String namePlace = line.get(7);
                        ArrayList<Place> listPlace = carRepairGarage.getListOfPlace();
                        int findPlace = carRepair.findByName(namePlace, listPlace);
                        if(findPlace > -1){
                            place = listPlace.get(findPlace);
                        }
                        else {
                            System.out.println("место "+namePlace+" Не найдено.");
                            System.out.println("Заказ "+name + " не будет добавлен");
                            continue;
                        }
                    } catch (NumberFormatException e){
                        System.err.println("Неправильные данные");
                        return;
                    }
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
                        create = LocalDateTime.parse(line.get(4), formatter);
                        start = LocalDateTime.parse(line.get(5), formatter);
                        complete = LocalDateTime.parse(line.get(6), formatter);
                    }
                    catch (DateTimeParseException e){
                        System.err.println("произошла ошибка при парсинге времени заказа "+name);
                        continue;
                    }
                    int findOrder = carRepair.findById(id, orders);
                    if(findOrder > -1){
                        orderDB.deleteOrderInDB(id);
                        carRepairOrders.addOrder(id, name, cost, place, create, start, complete);
                    }
                    else{
                        carRepairOrders.addOrder(id, name, cost, place, create, start, complete);
                    }
                }
            }
        }
    }
    public void importGarage(){
        boolean next = DeleteAllAgree();
        if(!next) return;
        String nameFile = workWithFile.whereFromImport(config.getStandartFileCsvGarage());
        if(nameFile.equals("???")){
            return;
        }
        ArrayList<ArrayList<String>> data = workWithFile.importData(nameFile);
        if(!data.isEmpty()){
            ArrayList<Place> places = carRepairGarage.getListOfPlace();
            for(ArrayList<String> line : data){
                if(line.size() != 2){
                    System.out.println("Неправильная таблица данных");
                    return;
                }
                else {
                    try {
                        int id = Integer.parseInt(line.get(0));
                        String name = line.get(1);
                        int findPlace = carRepair.findById(id, places);
                        if(findPlace > -1){
                            placeBD.deletePlaceInDB(id);
                            carRepairGarage.addPlace(id, name);
                        }
                        else {
                            carRepairGarage.addPlace(id, name);
                        }
                    } catch (NumberFormatException e){
                        System.err.println("Неправильные данные");
                        return;
                    }
                }
            }
        }
    }

    public void importMaster() {
        boolean next = DeleteAllAgree();
        if(!next) return;
        ArrayList<Master> masters = carRepairMaster.getListOfMasters(SortTypeMaster.ID);
        String nameFile = workWithFile.whereFromImport(config.getStandartFileCsvMaster());
        if(nameFile.equals("???")){
            return;
        }
        ArrayList<ArrayList<String>> data = workWithFile.importData(nameFile);
        if (!data.isEmpty()) {
            for (ArrayList<String> line : data) {
                if (line.size() != 3) {
                    System.out.println("Неправильная таблица данных");
                    return;
                } else {
                    try {
                        int id = Integer.parseInt(line.get(0));
                        String name = line.get(1);
                        ArrayList<String> strIdOrders = new ArrayList<>();
                        ArrayList<Integer> idOrders = new ArrayList<>();
                        if (!line.get(2).equals("none")) {
                            strIdOrders = new ArrayList<>(Arrays.asList(line.get(2).split(";")));
                            for(String idOrder : strIdOrders){
                                idOrders.add(Integer.parseInt(idOrder));
                            }
                        }
                        int findMaster = carRepair.findById(id, masters);
                        if (findMaster > -1) {
                            masterDB.deleteMasterInDB(id);
                            carRepairMaster.addMaster(id, name, idOrders);
                        } else {
                            carRepairMaster.addMaster(id, name, idOrders);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Неправильные данные");
                        return;
                    }
                }
            }
        }
    }
}
