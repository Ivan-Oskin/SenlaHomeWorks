package com.oskin.task6.Controller;

import com.oskin.task6.Model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class CarRepair {

    //Метод для поиска

    public static  <T extends Nameable> int findByName(String name, ArrayList<T> list) {
        int number = -1;
        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getName())) {
                number = i;
                break;
            }
        }
        return number;
    }
    public static  <T extends Nameable> int findById(int id, ArrayList<T> list) {
        int number = -1;
        for(T element : list){
            if(id == element.getId())
                return list.indexOf(element);
        }
        return number;
    }


    // Метод для удаления

    public static  <T extends Nameable> boolean delete(String name, ArrayList<T> list) {
        int i = findByName(name, list);
        if (i == -1) {
            return false;
        } else {
            list.remove(i);
            return true;
        }
    }

    //Количество свободных мест на любую дату
    public static int getCountFreeTime(LocalDateTime date) {
        int countPlace = CarRepairGarage.getInstance().getFreePlace(date).size();
        if (countPlace == 0) return 0;
        int countMaster = CarRepairMaster.getInstance().getListOfMasters(SortTypeMaster.ID).size();
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0);
        LocalDateTime finish = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 0);
        ArrayList<Order> ordersByTime =CarRepairOrders.getInstance().getOrdersInTime(StatusOrder.ACTIVE, start, finish, SortTypeOrder.START);
        for (int i = 0; i < ordersByTime.size(); i++) {
            if (ordersByTime.get(i).getTimeStart().compareTo(date) <= 0 && ordersByTime.get(i).getTimeComplete().compareTo(date) >= 0) {
                countMaster -=CarRepairMaster.getInstance().getMastersByOrder(ordersByTime.get(i).getName()).size();
            }
        }
        if (countPlace > countMaster) {
            return countMaster;
        } else {
            return countPlace;
        }
    }

    public static LocalDateTime getNearestDate(LocalDateTime fromDate) {
        LocalDateTime date = LocalDateTime.from(fromDate);
        if(CarRepairMaster.getInstance().getListOfMasters(SortTypeMaster.ID).isEmpty() ||
                CarRepairOrders.getInstance().getListOfOrders(SortTypeOrder.ID).isEmpty()){
            return null;
        }
        while (true) {
            int k = getCountFreeTime(date);
            if (k > 0) {
                return date;
            } else {
                if (date.getDayOfWeek().getValue() >= 6) {
                    date = date.plusDays(8 - date.getDayOfWeek().getValue());
                    date = date.withHour(10);
                }
                if (date.getHour() < 10) {
                    date = date.withHour(10);
                } else if (date.getHour() >= 18) {
                    date = date.plusDays(1);
                    date = date.withHour(10);
                } else {
                    date = date.plusHours(1);
                }
            }
        }
    }

    public static void exportData(ArrayList<String> dataString, String fileName){
        String name = fileName+".csv";
        try(FileWriter writer = new FileWriter(name)){
            for(String line : dataString){
                writer.append(line);
            }
        }
        catch (IOException e){
            System.err.println("\nОшибка при работе с файлом\n");
        }

    }

    public static ArrayList<ArrayList<String>> importData(String fileName){
        String name = fileName+".csv";
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(name))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null){
                ArrayList<String> mas =new ArrayList<>(Arrays.asList(line.split(",")));
                data.add(mas);
            }
        }
        catch (IOException e){
            System.err.println("\nОшибка при работе с файлом\n");
        }
        return data;
    }
}



