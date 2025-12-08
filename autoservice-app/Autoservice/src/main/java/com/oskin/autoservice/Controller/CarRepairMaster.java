package com.oskin.autoservice.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.autoservice.Model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class CarRepairMaster {
    private static CarRepairMaster instance;

    private CarRepairMaster() {

    }

    public static CarRepairMaster getInstance() {
        if (instance == null) {
            instance = new CarRepairMaster();
        }
        return instance;
    }

    private ArrayList<Master> masters = new ArrayList<>();

    public void addMaster(int id, String name) {
        Master master = new Master(id, name);
        masters.add(master);
    }

    public void addMaster(int id, String name, ArrayList<String> listOfOrder) {
        Master master = new Master(id, name, listOfOrder);
        masters.add(master);
    }

    public boolean deleteMaster(String name) {
        return CarRepair.delete(name, masters);
    }

    public ArrayList<Master> getListOfMasters(SortTypeMaster sortType) {
        switch (sortType) {
            case ID:
                masters.sort(Comparator.comparing(Master::getId));
                break;
            case ALPHABET:
                masters.sort(Comparator.comparing(Master::getName));
                break;
            case BUSYNESS:
                masters.sort(Comparator.comparing(Master::getCountOfOrders));
                break;
        }
        return masters;
    }

    public boolean setOrderToMaster(String nameMaster, String nameOrder) {
        ArrayList<Order> listOfOrders = CarRepairOrders.getInstance().getListOfOrders(SortTypeOrder.ID);
        int i = CarRepair.findByName(nameMaster, masters);
        int j = CarRepair.findByName(nameOrder, listOfOrders);
        if (i >= 0 && j >= 0) {
            Master master = masters.get(i);
            master.addOrder(listOfOrders.get(j));
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Master> getMastersByOrder(String name) {
        ArrayList<Master> newList = new ArrayList<>();
        for (int i = 0; i < masters.size(); i++) {
            Master master = masters.get(i);
            if (master.getNamesOfOrder().contains(name)) {
                newList.add(master);
            }
        }
        return newList;
    }

    public void exportMaster() {
        ArrayList<String> dataList = new ArrayList<>(masters.size() + 1);
        dataList.add("ID,NAME,ORDERS\n");
        for (Master master : getListOfMasters(SortTypeMaster.ID)) {
            int id = master.getId();
            String name = master.getName();
            String orders = String.join(";", master.getNamesOfOrder());
            if (orders.isEmpty()) orders = "none";
            dataList.add(id + "," + name + "," + orders + "\n");
        }
        WorkWithFile.whereExport(dataList, FileName.MASTER);
    }

    public void importMaster() {
        String nameFile = WorkWithFile.whereFromImport("Master");
        if(nameFile.equals("???")){
            return;
        }
        ArrayList<ArrayList<String>> data = WorkWithFile.importData(nameFile);
        if (!data.isEmpty()) {
            for (ArrayList<String> line : data) {
                if (line.size() != 3) {
                    System.out.println("Неправильная таблица данных");
                    return;
                } else {
                    try {
                        int id = Integer.parseInt(line.get(0));
                        String name = line.get(1);
                        ArrayList<String> nameOrders = new ArrayList<>();
                        if (!line.get(2).equals("none")) {
                            nameOrders = new ArrayList<>(Arrays.asList(line.get(2).split(";")));
                        }
                        int findMaster = CarRepair.findById(id, masters);
                        if (findMaster > -1) {
                            if (!(masters.get(findMaster).getName().equals(name) && masters.get(findMaster).getNamesOfOrder().equals(nameOrders))) {
                                masters.set(findMaster, new Master(id, name, nameOrders));
                            } else {
                                continue;
                            }
                        } else {
                            addMaster(id, name, nameOrders);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Неправильные данные");
                        return;
                    }
                }
            }
        }
    }
    public void saveMaster(){
        WorkWithFile.serialization(masters, FileName.MASTER.getNAME()+".json");
    }
    public void loadMaster(){
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(FileName.MASTER.getNAME()+".json");
        if(file.exists()){
            try{
                masters = mapper.readValue(file, new TypeReference<ArrayList<Master>>() {});
            }
            catch (IOException e){
                System.err.println("Произошла ошибка при работе с файлом");
            }
        }
        else{
            try {
                file.createNewFile();
            }
            catch (IOException e){
                System.err.println("произошла ошибка при создании файла");
            }
        }
    }
}
