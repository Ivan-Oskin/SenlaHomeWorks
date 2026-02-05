package com.oskin.autoservice.controller;

import com.oskin.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;


@Controller
public class WorkWithFile {
    private final Config config;

    private final Logger logger = LoggerFactory.getLogger(WorkWithFile.class);
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public WorkWithFile(Config config) {
        this.config = config;
    }

    public int inputInt() {
        int input = 0;
        try {
            input = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            logger.error("Надо ввести только цифру!!!");
        }
        return input;
    }

    public void exportData(ArrayList<String> dataString, String fileName, boolean isStandard) {
        String name;
        if (!fileName.endsWith(".csv")) {
            name = fileName + ".csv";
        } else {
            name = fileName;
        }
        File file;
        if (isStandard) {
            Path path = Paths.get(config.getStandardPathToData() + name);
            file = path.toFile();
        } else {
            file = new File(name);
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (String line : dataString) {
                writer.append(line);
            }
        } catch (IOException e) {
            System.err.println("\nОшибка при работе с файлом\n");
        }
    }

    public void whereExport(ArrayList<String> dataList, String nameObject) {
        System.out.println("Куда экспортировать данные " + nameObject + "?\n" +
                "1. " + nameObject + " 2. Выбрать другой файл 0. Выход");
        int input;
        while (true) {
            input = inputInt();
            if (input >= 0 && input < 3) break;
        }
        switch (input) {
            case 1:
                exportData(dataList, nameObject, true);
                System.out.println("Данные экспортированы");
                logger.info("successfully export");
                break;
            case 2:
                Scanner scanner = new Scanner(System.in);
                System.out.println("Введите имя файла");
                String nameFile = scanner.nextLine();
                exportData(dataList, nameFile, false);
                System.out.println("Данные экспортированы");
                logger.info("successfully export");
                break;
        }
    }

    public ArrayList<ArrayList<String>> importData(String fileName) {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                ArrayList<String> mas = new ArrayList<>(Arrays.asList(line.split(",")));
                data.add(mas);
            }
        } catch (IOException e) {
            System.err.println("\nОшибка при работе с файлом\n");
        }
        return data;
    }

    public String whereFromImport(String fileName) {
        System.out.println("Откуда импортировать данные " + fileName + "?\n" +
                "1. " + fileName + " 2. Другой файл формата .csv 0. Выход");
        int input;
        while (true) {
            input = inputInt();
            if (input >= 0 && input < 3) break;
        }
        switch (input) {
            case 1:
                return config.getStandardPathToData() + fileName;
            case 2:
                Scanner scanner = new Scanner(System.in);
                System.out.println("Введите имя файла");
                String nextFileName;
                while (true) {
                    nextFileName = scanner.nextLine();
                    if (nextFileName.endsWith(".csv")) break;
                    System.out.println("Файл должен быть расширения .csv");
                    return nextFileName;
                }
            default:
                return "???";
        }
    }
}
