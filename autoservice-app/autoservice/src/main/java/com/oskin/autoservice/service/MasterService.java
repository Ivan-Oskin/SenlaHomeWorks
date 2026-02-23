package com.oskin.autoservice.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class MasterService {
    Config config;
    MasterRepository masterRepository;
    OrderMasterService orderMasterService;
    OrderMasterRepository orderMasterRepository;
    OrderRepository orderRepository;

    private final Logger logger = LoggerFactory.getLogger(MasterService.class);

    @Autowired
    public MasterService(Config config, MasterRepository masterRepository,
                         OrderMasterService orderMasterService, OrderMasterRepository orderMasterRepository,
                         OrderRepository orderRepository) {
        this.config = config;
        this.masterRepository = masterRepository;
        this.orderMasterService = orderMasterService;
        this.orderMasterRepository = orderMasterRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void addMaster(int id, String name) {
        Master master = new Master(id, name);
        masterRepository.create(master);
    }
    @Transactional
    public boolean deleteMaster(String name) {
        Master master = masterRepository.find(name);
        if (master != null) {
            orderMasterService.deleteByMaster(master.getId());
            masterRepository.delete(master.getId());
            return true;
        }
        return false;
    }

    public Master findMaster(int id) {
        return masterRepository.find(id);
    }

    @Transactional
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
            return orderMasterService.getMasterFromOrderMaster(orderMasters);
        }
        return new ArrayList<>();
    }
}
