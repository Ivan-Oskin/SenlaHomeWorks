package com.oskin.autoservice.Controller;

import com.oskin.autoservice.Model.*;
import com.oskin.autoservice.View.CarRepairInput;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
        String name;
        if(!fileName.endsWith(".csv")){
            name = fileName+".csv";
        }
        else {
            name = fileName;
        }
        try(FileWriter writer = new FileWriter(name)){
            for(String line : dataString){
                writer.append(line);
            }
        }
        catch (IOException e){
            System.err.println("\nОшибка при работе с файлом\n");
        }

    }

    public static void whereExport(ArrayList<String> dataList, FileName nameObject){
        System.out.println("Куда экспортировать данные "+nameObject.getNAME() +"?\n" +
                "1. "+nameObject.getNAME()+".csv 2. Выбрать другой файл 0. Выход");
        int input = 0;
        while (true){
            input = CarRepairInput.getInstance().inputInt();
            if(input >= 0 && input < 3) break;
        }
        switch (input){
            case 1:{
                exportData(dataList, nameObject.getNAME());
                System.out.println("Данные экспортированы");
                break;
            }
            case 2:
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Введите имя файла");
                String nameFile = scanner.nextLine();
                exportData(dataList, nameFile);
                System.out.println("Данные экспортированы");
                break;
            }
            default:
            {
                return;
            }
        }
    }

    public static ArrayList<ArrayList<String>> importData(String fileName){
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
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
    public static String whereFromImport(FileName fileName){
        System.out.println("Откуда импортировать данные "+fileName.getNAME()+"?\n" +
                "1. "+fileName.getNAME()+".csv"+" 2. Другой файл формата .csv 0. Выход");
        int input = 0;
        while (true){
            input = CarRepairInput.getInstance().inputInt();
            if(input >= 0 && input < 3) break;
        }
        switch (input){
            case 1:{
                return fileName.getNAME()+".csv";
            }
            case 2:
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Введите имя файла");
                String nextFileName;
                while (true){
                    nextFileName = scanner.nextLine();
                    if(nextFileName.endsWith(".csv")) break;
                    System.out.println("Файл должен быть расширения .csv");
                }
                return nextFileName;
            }
            default:
            {
                return "???";
            }
        }
    }
}



