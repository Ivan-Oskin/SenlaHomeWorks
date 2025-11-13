package com.oskin.task5.Model;
import java.util.ArrayList;
import java.util.Comparator;

public class CarRepairMaster {
    private static CarRepairMaster instance;

    private CarRepairMaster(){

    }

    public static CarRepairMaster getInstance(){
        if(instance == null){
            instance = new CarRepairMaster();
        }
        return instance;
    }

    private ArrayList<Master> masters = new ArrayList<>();

    public void addMaster(String name) {
        Master master = new Master(name);
        masters.add(master);
    }
    public boolean deleteMaster(String name) {
        return CarRepair.delete(name, masters);
    }

    public ArrayList<Master> getListOfMasters(boolean sortByAlphabet) {
        ArrayList<Master> newList = new ArrayList<>(masters);
        if (sortByAlphabet) {
            newList.sort(Comparator.comparing(Master::getName));
        } else {
            newList.sort(Comparator.comparing(Master::getCountOfOrders));
        }
        return newList;
    }
    public boolean setOrderToMaster(String nameMaster, String nameOrder) {
        ArrayList<Order> listOfOrders = CarRepairOrders.getInstance().getListOfOrders(SortType.START);
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



}
