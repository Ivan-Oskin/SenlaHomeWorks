package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Order;

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
    FunctionsDB functionsDB;
    public void addOrdersByMasterInDB(int id, int idMaster, int idOrder){
        String sql = "INSERT INTO order_master (id, master_id, order_id) VALUES (?, ?, ?)";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, idMaster);
            statement.setInt(3, idOrder);
            statement.executeUpdate();
            functionsDB.commit();
        } catch (java.sql.SQLException e){
            functionsDB.rollback();
            e.printStackTrace();
        }
    }
    public int getMaxIdLink(){
        int result = -1;
        try(Statement statement = connectionDB.getConnection().createStatement()){
            ResultSet set = statement.executeQuery("SELECT id FROM order_master ORDER BY id DESC LIMIT 1");
            while (set.next()){
                result = set.getInt("id");
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Integer> selectIdOrdersByMaster(int idMaster){
        ArrayList<Integer> idOrders = new ArrayList<>();
        try(Statement statement = connectionDB.getConnection().createStatement()){
            ResultSet setOrdersByMaster = statement.executeQuery("SELECT * FROM order_master WHERE master_id = " + idMaster);
            while (setOrdersByMaster.next()){
                idOrders.add(setOrdersByMaster.getInt("order_id"));
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return idOrders;
    }
    public void deleteLinkInDB(int id_master){
        String sql = "DELETE FROM order_master WHERE master_id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id_master);
            statement.executeUpdate();
            functionsDB.commit();
        } catch (java.sql.SQLException e){
            functionsDB.rollback();
            e.printStackTrace();
        }
    }
    public ArrayList<Integer> getMastersByOrderInDB(String name) {
        Order order = orderDB.findOrderInDB(name);
        ArrayList<Integer> mastersId = new ArrayList<>();
        String sql = "SELECT master_id FROM order_master WHERE order_id = ? ";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            if(order != null){
                statement.setInt(1, order.getId());
                ResultSet set = statement.executeQuery();
                while (set.next()){
                    mastersId.add(set.getInt("master_id"));
                }
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return mastersId;
    }
}
