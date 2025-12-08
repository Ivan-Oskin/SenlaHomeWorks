package com.oskin.autoservice.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oskin.autoservice.Model.FileName;
import com.oskin.autoservice.Model.Nameable;
import com.oskin.autoservice.View.CarRepairInput;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class WorkWithFile {
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
    public static String whereFromImport(String fileName){
        System.out.println("Откуда импортировать данные "+fileName+"?\n" +
                "1. "+fileName+".csv"+" 2. Другой файл формата .csv 0. Выход");
        int input = 0;
        while (true){
            input = CarRepairInput.getInstance().inputInt();
            if(input >= 0 && input < 3) break;
        }
        switch (input){
            case 1:{
                return fileName+".csv";
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

    public static <T extends Nameable> void serialization(ArrayList<T> array, String fileName){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try{
            mapper.writeValue(new File(fileName), array);
        }
        catch (IOException e){
            System.err.println("Произошла ошибка при работе с файлом");
        }
    }
}
