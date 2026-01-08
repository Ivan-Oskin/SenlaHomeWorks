package com.oskin.autoservice.Controller;
import com.oskin.Annotations.*;
import com.oskin.autoservice.View.CarRepairInput;
import com.oskin.config.Config;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


@Singleton
public class WorkWithFile {
    @Inject
    private CarRepairInput carRepairInput;
    @Inject
    private Config config;
    public void exportData(ArrayList<String> dataString, String fileName, boolean isStandart){
        String name;
        if(!fileName.endsWith(".csv")){
            name = fileName+".csv";
        }
        else {
            name = fileName;
        }
        File file;
        if(isStandart){
            Path path = Paths.get(config.getStandartPathToData()+name);
            file = path.toFile();
        }
        else {
            file = new File(name);
        }
        try(FileWriter writer = new FileWriter(file)){
            for(String line : dataString){
                writer.append(line);
            }
        }
        catch (IOException e){
            System.err.println("\nОшибка при работе с файлом\n");
        }

    }

    public void whereExport(ArrayList<String> dataList, String nameObject){
        System.out.println("Куда экспортировать данные "+nameObject +"?\n" +
                "1. "+nameObject+" 2. Выбрать другой файл 0. Выход");
        int input = 0;
        while (true){
            input = carRepairInput.inputInt();
            if(input >= 0 && input < 3) break;
        }
        switch (input){
            case 1:{
                exportData(dataList, nameObject, true);
                System.out.println("Данные экспортированы");
                break;
            }
            case 2:
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Введите имя файла");
                String nameFile = scanner.nextLine();
                exportData(dataList, nameFile, false);
                System.out.println("Данные экспортированы");
                break;
            }
            default:
            {
                return;
            }
        }
    }
    public ArrayList<ArrayList<String>> importData(String fileName){
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
    public String whereFromImport(String fileName){
        System.out.println("Откуда импортировать данные "+fileName+"?\n" +
                "1. "+fileName+" 2. Другой файл формата .csv 0. Выход");
        int input = 0;
        while (true){
            input = carRepairInput.inputInt();
            if(input >= 0 && input < 3) break;
        }
        switch (input){
            case 1:{
                return config.getStandartPathToData()+fileName;
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
