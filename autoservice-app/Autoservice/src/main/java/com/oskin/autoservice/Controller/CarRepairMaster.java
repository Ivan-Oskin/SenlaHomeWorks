package com.oskin.autoservice.Controller;
import com.oskin.Annotations.Inject;
import com.oskin.Annotations.Singleton;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.Model.Master;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.SortTypeMaster;
import com.oskin.autoservice.Model.OrderMaster;
import com.oskin.config.Config;
import java.util.ArrayList;

import static org.postgresql.hostchooser.HostRequirement.master;

@Singleton
public final class CarRepairMaster {
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;
    @Inject
    MasterRepository masterRepository;
    @Inject
    CarRepairOrderMaster carRepairOrderMaster;
    @Inject
    OrderMasterRepository orderMasterRepository;
    @Inject
    OrderRepository orderRepository;
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
        masterRepository.create(master);
    }
    public boolean deleteMaster(String name) {
        Master master = masterRepository.find(name);
        if (master != null) {
            masterRepository.delete(master.getId());
            carRepairOrderMaster.deleteByMaster(master.getId());
            return true;
        }
        return false;
    }
    public ArrayList<Master> getListOfMasters(SortTypeMaster sortType) {
        ArrayList<Master> masters = masterRepository.findAll(sortType);
        return masters;
    }

    public ArrayList<Master> getMastersByOrder(String name) {
        Order order = orderRepository.find(name);
        if (master != null) {
            ArrayList<OrderMaster> orderMasters = orderMasterRepository.getMastersByOrderInDB(order.getId());
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
