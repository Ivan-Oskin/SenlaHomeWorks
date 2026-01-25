package com.oskin.autoservice.controller;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.repository.PlaceRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    OrderRepository orderRepository;
    @Inject
    CarRepairGarage carRepairGarage;
    @Inject
    CarRepairMaster carRepairMaster;
    @Inject
    MasterRepository masterRepository;
    @Inject
    PlaceRepository placeRepository;
    @Inject
    OrderMasterRepository orderMasterRepository;
    Logger logger = LoggerFactory.getLogger(ImportDates.class);

    public int inputInt() {
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try {
            input = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.err.println("\nНадо ввести только цифру!!!\n");
        }
        return input;
    }

    public boolean replaceAgree() {
        while (true) {
            System.out.println("Что делать при совпадении?.\n 1 - Заменить существующее \n 2 - Оставить существующее");
            int x = inputInt();
            if (x == 1) return true;
            else if (x == 2) return false;
        }
    }

    public void importOrder() {
        logger.info("Start import order");
        boolean replace = replaceAgree();
        String nameFile = workWithFile.whereFromImport(config.getStandardFileCsvOrders());
        if (nameFile.equals("???")) {
            return;
        }
        ArrayList<ArrayList<String>> data = workWithFile.importData(nameFile);
        if (!data.isEmpty()) {
            logger.info("File found and opened successfully");
            for (ArrayList<String> line : data) {
                if (line.size() != 8) {
                    System.out.println("Неправильная таблица данных");
                    return;
                } else {
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
                        for (StatusOrder statusOrder : StatusOrder.values()) {
                            if (statusOrder.getSTATUS().equals(statusString)) {
                                status = statusOrder;
                                break;
                            }
                        }
                        if (status == null) {
                            System.err.println("Неправильные данные в заказе " + name);
                            continue;
                        }
                        int PlaceId = Integer.parseInt(line.get(7));
                        place = placeRepository.find(PlaceId);
                        if (place == null) {
                            System.out.println("место с id " + PlaceId + " Не найдено.");
                            System.out.println("Заказ " + name + "не будет добавлен");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Неправильные данные");
                        return;
                    }
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
                        create = LocalDateTime.parse(line.get(4), formatter);
                        start = LocalDateTime.parse(line.get(5), formatter);
                        complete = LocalDateTime.parse(line.get(6), formatter);
                    } catch (DateTimeParseException e) {
                        System.err.println("произошла ошибка при парсинге времени заказа " + name);
                        continue;
                    }
                    Order order = orderRepository.find(id);
                    if (order != null && replace) {
                        orderRepository.update(new Order(id, name, cost, place, create, start, complete, status));
                    } else if (order == null) {
                        carRepairOrders.addOrder(id, name, cost, place, create, start, complete, status);
                    }
                }
            }
            logger.info("successfully import order");
        }
    }

    public void importGarage() {
        logger.info("Start import garage");
        boolean replace = replaceAgree();
        String nameFile = workWithFile.whereFromImport(config.getStandardFileCsvGarage());
        if (nameFile.equals("???")) {
            return;
        }
        ArrayList<ArrayList<String>> data = workWithFile.importData(nameFile);
        if (!data.isEmpty()) {
            logger.info("File found and opened successfully");
            for (ArrayList<String> line : data) {
                if (line.size() != 2) {
                    System.out.println("Неправильная таблица данных");
                    return;
                } else {
                    try {
                        int id = Integer.parseInt(line.get(0));
                        String name = line.get(1);
                        Place place = placeRepository.find(id);
                        if (place != null && replace) {
                            placeRepository.update(new Place(id, name));
                        } else if (place == null) {
                            carRepairGarage.addPlace(id, name);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Неправильные данные");
                        return;
                    }
                }
            }
            logger.info("successfully import place");
        }
    }

    public void importMaster() {
        logger.info("Start import master");
        boolean replace = replaceAgree();
        String nameFile = workWithFile.whereFromImport(config.getStandardFileCsvMaster());
        if (nameFile.equals("???")) {
            return;
        }
        ArrayList<ArrayList<String>> data = workWithFile.importData(nameFile);
        if (!data.isEmpty()) {
            for (ArrayList<String> line : data) {
                logger.info("File found and opened successfully");
                if (line.size() != 2) {
                    System.out.println("Неправильная таблица данных");
                    return;
                } else {
                    try {
                        int id = Integer.parseInt(line.get(0));
                        String name = line.get(1);
                        Master master = masterRepository.find(id);
                        if (master != null && replace) {
                            masterRepository.update(new Master(id, name));
                        } else if (master == null) {
                            carRepairMaster.addMaster(id, name);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Неправильные данные");
                        return;
                    }
                }
            }
            logger.info("successfully import master");
        }
    }

    public void importOrderMaster() {
        logger.info("Start import orderMaster");
        boolean replace = replaceAgree();
        String nameFile = workWithFile.whereFromImport(config.getStandardFileCsvOrderMaster());
        if (nameFile.equals("???")) {
            return;
        }
        ArrayList<ArrayList<String>> data = workWithFile.importData(nameFile);
        if (!data.isEmpty()) {
            for (ArrayList<String> line : data) {
                if (line.size() != 3) {
                    logger.info("File found and opened successfully");
                    System.out.println("Неправильная таблица данных");
                    return;
                } else {
                    try {
                        int id = Integer.parseInt(line.get(0));
                        int master_id = Integer.parseInt(line.get(1));
                        int order_id = Integer.parseInt(line.get(2));
                        OrderMaster orderMaster = orderMasterRepository.find(id);
                        if (orderMaster != null && replace) {
                            orderMasterRepository.update(new OrderMaster(id, orderRepository.find(order_id), masterRepository.find(master_id)));
                        } else if (orderMaster == null) {
                            orderMasterRepository.create(id, master_id, order_id);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Неправильные данные");
                        return;
                    }
                }
            }
            logger.info("successfully import orderMaster");
        }
    }
}
