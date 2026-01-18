package com.oskin.autoservice.Controller;
import com.oskin.Annotations.Inject;
import com.oskin.autoservice.DAO.*;
import com.oskin.autoservice.Model.*;
import com.oskin.config.Config;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ImportDates {

    @Inject
    CarRepairOrders carRepairOrders;
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;
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

    public int inputInt(){
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try{
            input = scanner.nextInt();
            scanner.nextLine();
        }
        catch (InputMismatchException e){
            scanner.nextLine();
            System.err.println("\nНадо ввести только цифру!!!\n");
        }
        return input;
    }

    public boolean ReplaceAgree(){
        while (true){
            System.out.println("Что делать при совпадении?.\n 1 - Заменить существующее \n 2 - Оставить существующее");
            int x = inputInt();
            if(x == 1) return true;
            else if(x == 2) return false;
        }
    }

    public void importOrder(){
        boolean replace = ReplaceAgree();
        String nameFile = workWithFile.whereFromImport(config.getStandardFileCsvOrders());
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
                        int PlaceId = Integer.parseInt(line.get(7));
                        place = placeBD.findPlaceInDb(PlaceId);
                        if(place == null){
                            System.out.println("место с id "+PlaceId+" Не найдено.");
                            System.out.println("Заказ "+name + "не будет добавлен");
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
                    Order order = orderDB.findOrderInDB(id);
                    if(order != null && replace){
                        orderDB.deleteOrderInDB(id);
                        carRepairOrders.addOrder(id, name, cost, place, create, start, complete);
                    }
                    else if(order == null){
                        carRepairOrders.addOrder(id, name, cost, place, create, start, complete);
                    }
                }
            }
        }
    }
    public void importGarage(){
        boolean replace = ReplaceAgree();
        String nameFile = workWithFile.whereFromImport(config.getStandardFileCsvGarage());
        if(nameFile.equals("???")){
            return;
        }
        ArrayList<ArrayList<String>> data = workWithFile.importData(nameFile);
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
                        Place place = placeBD.findPlaceInDb(id);
                        if(place != null && replace){
                            placeBD.deletePlaceInDB(id);
                            carRepairGarage.addPlace(id, name);
                        }
                        else if(place == null) {
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
        boolean replace = ReplaceAgree();
        String nameFile = workWithFile.whereFromImport(config.getStandardFileCsvMaster());
        if(nameFile.equals("???")){
            return;
        }
        ArrayList<ArrayList<String>> data = workWithFile.importData(nameFile);
        if (!data.isEmpty()) {
            for (ArrayList<String> line : data) {
                if (line.size() != 2) {
                    System.out.println("Неправильная таблица данных");
                    return;
                } else {
                    try {
                        int id = Integer.parseInt(line.get(0));
                        String name = line.get(1);
                        Master master = masterDB.findMasterInDb(id);
                        if (master != null && replace) {
                            masterDB.deleteMasterInDB(id);
                            carRepairMaster.addMaster(id, name);
                        } else if(master == null) {
                            carRepairMaster.addMaster(id, name);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Неправильные данные");
                        return;
                    }
                }
            }
        }
    }

    public void importOrderMaster(){
        boolean replace = ReplaceAgree();
        String nameFile = workWithFile.whereFromImport(config.getStandardFileCsvOrderMaster());
        if(nameFile.equals("???")){
            return;
        }
        ArrayList<ArrayList<String>> data = workWithFile.importData(nameFile);
        if(!data.isEmpty()){
            for(ArrayList<String> line : data){
                if(line.size() != 3){
                    System.out.println("Неправильная таблица данных");
                    return;
                }
                else {
                    try {
                        int id = Integer.parseInt(line.get(0));
                        int master_id = Integer.parseInt(line.get(1));
                        int order_id = Integer.parseInt(line.get(2));
                        OrderMaster orderMaster = ordersByMasterDb.findOrderMasterInDb(id);
                        if(orderMaster != null && replace){
                            ordersByMasterDb.deleteLinkInDB(id);
                            ordersByMasterDb.addOrdersByMasterInDB(id,master_id, order_id);
                        }
                        else if(orderMaster == null) {
                            ordersByMasterDb.addOrdersByMasterInDB(id, master_id, order_id);
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
