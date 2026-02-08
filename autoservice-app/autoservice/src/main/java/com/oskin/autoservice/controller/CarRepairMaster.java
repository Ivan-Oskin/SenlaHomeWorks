package com.oskin.autoservice.controller;

import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.autoservice.model.SortTypeMaster;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;

@Controller
public class CarRepairMaster {
    WorkWithFile workWithFile;
    Config config;
    MasterRepository masterRepository;
    CarRepairOrderMaster carRepairOrderMaster;
    OrderMasterRepository orderMasterRepository;
    OrderRepository orderRepository;

    private final Logger logger = LoggerFactory.getLogger(CarRepairMaster.class);

    @Autowired
    public CarRepairMaster(WorkWithFile workWithFile, Config config, MasterRepository masterRepository,
                                    CarRepairOrderMaster carRepairOrderMaster, OrderMasterRepository orderMasterRepository,
                                    OrderRepository orderRepository) {
        this.workWithFile = workWithFile;
        this.config = config;
        this.masterRepository = masterRepository;
        this.carRepairOrderMaster = carRepairOrderMaster;
        this.orderMasterRepository = orderMasterRepository;
        this.orderRepository = orderRepository;
    }

    public void addMaster(int id, String name) {
        Master master = new Master(id, name);
        masterRepository.create(master);
    }
    public boolean deleteMaster(String name) {
        Master master = masterRepository.find(name);
        if (master != null) {
            carRepairOrderMaster.deleteByMaster(master.getId());
            masterRepository.delete(master.getId());
            return true;
        }
        return false;
    }

    public Master findMaster(int id) {
        return masterRepository.find(id);
    }

    public void updateMaster(Master master) {
        masterRepository.update(master);
    }

    public ArrayList<Master> getListOfMasters(SortTypeMaster sortType) {
        return masterRepository.findAll(sortType);
    }

    public ArrayList<Master> getMastersByOrder(String name) {
        Order order = orderRepository.find(name);
        if (order != null) {
            ArrayList<OrderMaster> orderMasters = orderMasterRepository.getMastersByOrderInDB(order.getId());
            return carRepairOrderMaster.getMasterFromOrderMaster(orderMasters);
        }
        return new ArrayList<>();
    }
    public void exportMaster() {
        logger.info("Start export master");
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
