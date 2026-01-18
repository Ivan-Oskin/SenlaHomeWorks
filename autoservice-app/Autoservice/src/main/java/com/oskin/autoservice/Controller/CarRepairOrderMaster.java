package com.oskin.autoservice.Controller;
import com.oskin.Annotations.Inject;
import com.oskin.autoservice.DAO.MasterDB;
import com.oskin.autoservice.DAO.OrderDB;
import com.oskin.autoservice.DAO.OrdersByMasterDb;
import com.oskin.autoservice.Model.Master;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.OrderMaster;
import com.oskin.config.Config;

import java.util.ArrayList;


public class CarRepairOrderMaster {
    @Inject
    MasterDB masterDB;
    @Inject
    OrderDB orderDB;
    @Inject
    OrdersByMasterDb ordersByMasterDb;
    @Inject
    WorkWithFile workWithFile;
    @Inject
    Config config;

    public ArrayList<Order> getOrderFromOrderMaster(ArrayList<OrderMaster> orderMasters){
        ArrayList<Order> orders = new ArrayList<>(orderMasters.size());
        for(OrderMaster orderMaster : orderMasters){
            orders.add(orderMaster.getOrder());
        }
        return orders;
    }

    public ArrayList<Master> getMasterFromOrderMaster(ArrayList<OrderMaster> orderMasters){
        ArrayList<Master> masters = new ArrayList<>(orderMasters.size());
        for(OrderMaster orderMaster : orderMasters){
            masters.add(orderMaster.getMaster());
        }
        return masters;
    }
    public boolean setOrderMaster(String nameMaster, String nameOrder) {
        Master master = masterDB.findMasterInDb(nameMaster);
        Order order = orderDB.findOrderInDB(nameOrder);
        if (master != null &&  order != null) {
            ArrayList<OrderMaster> orderMasters = ordersByMasterDb.getOrdersByMasterInDB(master.getId());
            ArrayList<Order> orders = getOrderFromOrderMaster(orderMasters);
            Order findOrder = orders.stream().filter(order1 -> order1.getId() == order.getId()).findFirst().orElse(null);
            if (findOrder == null) {
                int maxId = ordersByMasterDb.getMaxIdLink();
                int idLink = maxId != -1 ? maxId + 1 : 1;
                ordersByMasterDb.addOrdersByMasterInDB(idLink, master.getId(), order.getId());
                return true;
            }
        }
        return false;
    }
    public void exportOrderMaster() {
        ArrayList<OrderMaster> orderMastersArray = ordersByMasterDb.selectOrderMaster();
        ArrayList<String> dataList = new ArrayList<>(orderMastersArray.size() + 1);
        dataList.add("id,id_master,id_order\n");
        for (OrderMaster orderMaster : orderMastersArray) {
            int id = orderMaster.getId();
            int master_id = orderMaster.getMaster().getId();
            int order_id = orderMaster.getOrder().getId();
            dataList.add(id + "," + master_id + ","+ order_id+"," + "\n");
        }
        workWithFile.whereExport(dataList, config.getStandardFileCsvOrderMaster());
    }

    public void deleteOrderMaster(int id_master){
        ordersByMasterDb.deleteLinkByMasterInDB(id_master);
    }
}
