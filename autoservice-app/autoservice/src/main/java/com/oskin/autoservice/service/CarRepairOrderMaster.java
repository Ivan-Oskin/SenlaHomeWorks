package com.oskin.autoservice.service;

import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.autoservice.model.SortTypeOrderMaster;
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
public class CarRepairOrderMaster {
    MasterRepository masterRepository;
    OrderRepository orderRepository;
    OrderMasterRepository orderMasterRepository;
    WorkWithFile workWithFile;
    Config config;

    Logger logger = LoggerFactory.getLogger(CarRepairOrderMaster.class);

    @Autowired
    public CarRepairOrderMaster(MasterRepository masterRepository, OrderRepository orderRepository, OrderMasterRepository orderMasterRepository,
                                WorkWithFile workWithFile, Config config) {
        this.masterRepository = masterRepository;
        this.orderRepository = orderRepository;
        this.orderMasterRepository = orderMasterRepository;
        this.workWithFile = workWithFile;
        this.config = config;
    }

    @Transactional
    public void addOrderMaster(int id, int masterId, int orderId) {
        Master master = masterRepository.find(masterId);
        Order order = orderRepository.find(orderId);
        orderMasterRepository.create(new OrderMaster(id, order, master));
    }

    public ArrayList<Order> getOrderFromOrderMaster(ArrayList<OrderMaster> orderMasters) {
        ArrayList<Order> orders = new ArrayList<>(orderMasters.size());
        for (OrderMaster orderMaster : orderMasters) {
            orders.add(orderMaster.getOrder());
        }
        return orders;
    }

    public ArrayList<Master> getMasterFromOrderMaster(ArrayList<OrderMaster> orderMasters) {
        ArrayList<Master> masters = new ArrayList<>(orderMasters.size());
        for (OrderMaster orderMaster : orderMasters) {
            masters.add(orderMaster.getMaster());
        }
        return masters;
    }
    @Transactional
    public boolean setOrderMaster(String nameMaster, String nameOrder) {
        Master master = masterRepository.find(nameMaster);
        Order order = orderRepository.find(nameOrder);
        if (master != null && order != null) {
            ArrayList<OrderMaster> orderMasters = orderMasterRepository.getOrdersByMasterInDB(master.getId());
            ArrayList<Order> orders = getOrderFromOrderMaster(orderMasters);
            Order findOrder = orders.stream().filter(order1 -> order1.getId() == order.getId()).findFirst().orElse(null);
            if (findOrder == null) {
                int maxId = orderMasterRepository.getMaxIdLink();
                int idLink = maxId != -1 ? maxId + 1 : 1;
                orderMasterRepository.create(idLink, master.getId(), order.getId());
                return true;
            }
        }
        return false;
    }
    public void exportOrderMaster() {
        logger.info("Start export orderMaster");
        ArrayList<OrderMaster> orderMastersArray = orderMasterRepository.findAll(SortTypeOrderMaster.ID);
        ArrayList<String> dataList = new ArrayList<>(orderMastersArray.size() + 1);
        dataList.add("id,id_master,id_order\n");
        for (OrderMaster orderMaster : orderMastersArray) {
            int id = orderMaster.getId();
            int master_id = orderMaster.getMaster().getId();
            int order_id = orderMaster.getOrder().getId();
            dataList.add(id + "," + master_id + "," + order_id + "," + "\n");
        }
        workWithFile.whereExport(dataList, config.getStandardFileCsvOrderMaster());
    }
    @Transactional
    public void deleteByMaster(int idMaster) {
        orderMasterRepository.deleteByMaster(idMaster);
    }

    public OrderMaster findOrderMaster(int id) {
        return orderMasterRepository.find(id);
    }
    @Transactional
    public void updateOrderMaster(OrderMaster orderMaster) {
        orderMasterRepository.update(orderMaster);
    }

    public ArrayList<OrderMaster> getOrdersByMasterInDB(int masterId) {
        return orderMasterRepository.getOrdersByMasterInDB(masterId);
    }
}
