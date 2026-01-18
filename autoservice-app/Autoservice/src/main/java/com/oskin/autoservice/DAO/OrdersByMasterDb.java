package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Master;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.OrderMaster;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class OrdersByMasterDb {
    @Inject
    ConnectionDB connectionDB;
    @Inject
    OrderDB orderDB;
    @Inject
    MasterDB masterDB;
    @Inject
    FunctionsDB functionsDB;

    public ArrayList<OrderMaster> selectOrderMaster(){
        ArrayList<OrderMaster> orderMaster = new ArrayList<>();
        try(Statement statement = connectionDB.getConnection().createStatement()) {
            ResultSet setOrderMaster = statement.executeQuery("SELECT * FROM order_master");
            while (setOrderMaster.next()){
                int id = setOrderMaster.getInt("id");
                Order order = orderDB.findOrderInDB(setOrderMaster.getInt("order_id"));
                Master master = masterDB.findMasterInDb(setOrderMaster.getInt("master_id"));
                orderMaster.add(new OrderMaster(id, order, master));
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return orderMaster;
    }

    public void addOrdersByMasterInDB(int id, int idMaster, int idOrder) {
        String sql = "INSERT INTO order_master (id, master_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, idMaster);
            statement.setInt(3, idOrder);
            statement.executeUpdate();
            functionsDB.commit();
        } catch (java.sql.SQLException e) {
            functionsDB.rollback();
            e.printStackTrace();
        }
    }

    public int getMaxIdLink() {
        int result = -1;
        try (Statement statement = connectionDB.getConnection().createStatement()) {
            ResultSet set = statement.executeQuery("SELECT id FROM order_master ORDER BY id DESC LIMIT 1");
            while (set.next()) {
                result = set.getInt("id");
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<OrderMaster> getOrdersByMasterInDB(int idMaster) {
        ArrayList<OrderMaster> orderMasterArrayList = new ArrayList<>();
        String sql = "SELECT * FROM order_master WHERE master_id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idMaster);
            ResultSet setOrderMaster = statement.executeQuery();
            while (setOrderMaster.next()) {
                int id = setOrderMaster.getInt("id");
                Order order = orderDB.findOrderInDB(setOrderMaster.getInt("order_id"));
                Master master = masterDB.findMasterInDb(setOrderMaster.getInt("master_id"));
                OrderMaster orderMaster = new OrderMaster(id, order, master);
                orderMasterArrayList.add(orderMaster);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return orderMasterArrayList;
    }

    public void deleteLinkByMasterInDB(int id_master) {
        String sql = "DELETE FROM order_master WHERE master_id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id_master);
            statement.executeUpdate();
            functionsDB.commit();
        } catch (java.sql.SQLException e) {
            functionsDB.rollback();
            e.printStackTrace();
        }
    }

    public void deleteLinkInDB(int id) {
        String sql = "DELETE FROM order_master WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            functionsDB.commit();
        } catch (java.sql.SQLException e) {
            functionsDB.rollback();
            e.printStackTrace();
        }
    }


    public ArrayList<OrderMaster> getMastersByOrderInDB(int idOrder) {
        ArrayList<OrderMaster> orderMasterArrayList = new ArrayList<>();
        String sql = "SELECT * FROM order_master WHERE order_id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idOrder);
            ResultSet setOrderMaster = statement.executeQuery();
            while (setOrderMaster.next()) {
                int id = setOrderMaster.getInt("id");
                Order order = orderDB.findOrderInDB(setOrderMaster.getInt("order_id"));
                Master master = masterDB.findMasterInDb(setOrderMaster.getInt("master_id"));
                OrderMaster orderMaster = new OrderMaster(id, order, master);
                orderMasterArrayList.add(orderMaster);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return orderMasterArrayList;
    }

    public OrderMaster findOrderMasterInDb(int id){
        String sql = "SELECT * FROM order_master WHERE id = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                int master_id = set.getInt("master_id");
                int order_id = set.getInt("order_id");
                Master master = masterDB.findMasterInDb(master_id);
                Order order = orderDB.findOrderInDB(order_id);
                return new OrderMaster(id,order,master);
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
