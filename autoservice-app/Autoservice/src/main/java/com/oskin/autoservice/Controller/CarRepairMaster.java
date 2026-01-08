package com.oskin.autoservice.Controller;
import com.oskin.Annotations.*;
import com.oskin.autoservice.DAO.MasterDB;
import com.oskin.autoservice.DAO.OrderDB;
import com.oskin.autoservice.DAO.OrdersByMasterDb;
import com.oskin.autoservice.Model.*;
import com.oskin.config.Config;
import java.util.ArrayList;
import java.util.Comparator;


@Singleton
public class CarRepairMaster {
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;
    @Inject
    MasterDB masterDB;
    @Inject
    OrdersByMasterDb ordersByMasterDb;
    @Inject
    OrderDB orderDB;
    private static CarRepairMaster instance;
    private CarRepairMaster() {

    }
    public static CarRepairMaster getInstance() {
        if (instance == null) {
            instance = new CarRepairMaster();
        }
        return instance;
    }
    public void addMaster(int id, String name) {
        Master master = new Master(id, name);
        masterDB.addMasterInDB(master);
    }
    public void addMaster(int id, String name, ArrayList<Integer> listOfOrderId) {
        Master master = new Master(id, name);
        masterDB.addMasterInDB(master);
        for(int idOrder : listOfOrderId){
            int maxId = ordersByMasterDb.getMaxIdLink();
            int idLink = maxId!=-1?maxId+1:1;
            ordersByMasterDb.addOrdersByMasterInDB(idLink, id, idOrder);
        }
    }
    public boolean deleteMaster(String name) {
        return masterDB.deleteMasterInDB(name);
    }
    public ArrayList<Master> getListOfMasters(SortTypeMaster sortType) {
        ArrayList<Master> masters = masterDB.SelectMasters();
        switch (sortType) {
            case ID:
                masters.sort(Comparator.comparing(Master::getId));
                break;
            case ALPHABET:
                masters.sort(Comparator.comparing(Master::getName));
                break;
            case BUSYNESS:
                masters.sort(Comparator.comparing(Master::getCountOfOrdersId));
                break;
        }
        return masters;
    }
    public boolean setOrderToMaster(String nameMaster, String nameOrder) {
        Master master = masterDB.findMasterInDb(nameMaster);
        Order order = orderDB.findOrderInDB(nameOrder);
        if (master != null &&  order != null) {
            if (!master.getIdOfOrder().contains(order.getId())) {
                int maxId = ordersByMasterDb.getMaxIdLink();
                int idLink = maxId != -1 ? maxId + 1 : 1;
                ordersByMasterDb.addOrdersByMasterInDB(idLink, master.getId(), order.getId());
                return true;
            }
        }
        return false;
    }
    public ArrayList<Master> getMastersByOrder(String name) {
        ArrayList<Master> newList = new ArrayList<>();
        ArrayList<Integer> masterId = ordersByMasterDb.getMastersByOrderInDB(name);
        for(int id : masterId){
            Master master = masterDB.findMasterInDb(id);
            newList.add(master);
        }
        return newList;
    }
    public void exportMaster() {
        ArrayList<Master> masters = getListOfMasters(SortTypeMaster.ID);
        ArrayList<String> dataList = new ArrayList<>(masters.size() + 1);
        dataList.add("ID,NAME,ORDERS\n");
        for (Master master : masters) {
            int id = master.getId();
            String name = master.getName();
            ArrayList<String> idOrdersString = new ArrayList<>();
            for(int idOrder : master.getIdOfOrder()){
                String StrIdOrder = String.valueOf(idOrder);
                idOrdersString.add(StrIdOrder);
            }
            String orders = String.join(";", idOrdersString);
            if (orders.isEmpty()) orders = "none";
            dataList.add(id + "," + name + "," + orders + "\n");
        }
        workWithFile.whereExport(dataList, config.getStandartFileCsvMaster());
    }
}
