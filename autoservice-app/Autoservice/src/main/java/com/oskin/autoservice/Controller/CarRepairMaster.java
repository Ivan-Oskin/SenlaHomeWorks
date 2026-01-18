package com.oskin.autoservice.Controller;
import com.oskin.Annotations.*;
import com.oskin.autoservice.DAO.MasterDB;
import com.oskin.autoservice.DAO.OrderDB;
import com.oskin.autoservice.DAO.OrdersByMasterDb;
import com.oskin.autoservice.Model.*;
import com.oskin.config.Config;
import java.util.ArrayList;

import static org.postgresql.hostchooser.HostRequirement.master;

@Singleton
public class CarRepairMaster {
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;
    @Inject
    MasterDB masterDB;
    @Inject
    CarRepairOrderMaster carRepairOrderMaster;
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
    public boolean deleteMaster(String name) {
        Master master = masterDB.findMasterInDb(name);
        if(master != null) {
            masterDB.deleteMasterInDB(master.getId());
            carRepairOrderMaster.deleteOrderMaster(master.getId());
            return true;
        }
        return false;
    }
    public ArrayList<Master> getListOfMasters(SortTypeMaster sortType) {
        ArrayList<Master> masters = masterDB.SelectMasters(sortType);
        return masters;
    }

    public ArrayList<Master> getMastersByOrder(String name) {
        Order order = orderDB.findOrderInDB(name);
        if(master != null){
            ArrayList<OrderMaster> orderMasters = ordersByMasterDb.getMastersByOrderInDB(order.getId());
            return carRepairOrderMaster.getMasterFromOrderMaster(orderMasters);
        }
        return new ArrayList<>();
    }
    public void exportMaster() {
        ArrayList<Master> masters = getListOfMasters(SortTypeMaster.ID);
        ArrayList<String> dataList = new ArrayList<>(masters.size() + 1);
        dataList.add("ID,NAME\n");
        for (Master master : masters) {
            int id = master.getId();
            String name = master.getName();
            dataList.add(id + "," + name + "\n");
        }
        workWithFile.whereExport(dataList, config.getStandardFileCsvMaster());
    }
}
