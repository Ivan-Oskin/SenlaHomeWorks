package com.oskin.autoservice.Controller;
import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.SortTypeOrderMaster;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.Model.Master;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.OrderMaster;
import com.oskin.config.Config;

import java.util.ArrayList;


public class CarRepairOrderMaster {
    @Inject
    MasterRepository masterRepository;
    @Inject
    OrderRepository orderRepository;
    @Inject
    OrderMasterRepository orderMasterRepository;
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;

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
    public boolean setOrderMaster(String nameMaster, String nameOrder) {
        Master master = masterRepository.find(nameMaster);
        Order order = orderRepository.find(nameOrder);
        if (master != null &&  order != null) {
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

    public void deleteByMaster(int idMaster) {
        orderMasterRepository.deleteByMaster(idMaster);
    }
}
