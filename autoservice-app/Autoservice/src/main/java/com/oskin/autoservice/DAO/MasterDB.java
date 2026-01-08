package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Master;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MasterDB {
    @Inject
    ConnectionDB connectionDB;


    public ArrayList<Integer> SelectIdOrdersByMaster(int idMaster){
        ArrayList<Integer> idOrders = new ArrayList<>();
        try(Statement statement = connectionDB.getConnection().createStatement()){
            ResultSet setOrdersByMaster = statement.executeQuery("SELECT * FROM OrdersByMaster WHERE master_id = " + idMaster);
            while (setOrdersByMaster.next()){
                idOrders.add(setOrdersByMaster.getInt("order_id"));
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return idOrders;
    }

    public ArrayList<Master> SelectMasters(){
        ArrayList<Master> masters = new ArrayList<>();
        try(Statement statement = connectionDB.getConnection().createStatement()){
            ResultSet setMaster = statement.executeQuery("SELECT * FROM Masters");
            while (setMaster.next()){
                int id = setMaster.getInt("id");
                String name  = setMaster.getString("name");
                Master master = new Master(id, name);
                master.addArrayOrdersId(SelectIdOrdersByMaster(id));
                masters.add(master);
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return masters;
    }

        public Master SelectMasterByName(String name){
            String sql = "SELECT * FROM Masters WHERE name = ?";
            try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)){
                statement.setString(1, name);
                ResultSet setMaster = statement.executeQuery();
                while (setMaster.next()){
                    int id = setMaster.getInt("id");
                    String nameMaster  = setMaster.getString("name");
                    Master master = new Master(id, nameMaster);
                    master.addArrayOrdersId(SelectIdOrdersByMaster(id));
                    return master;
                }
            } catch (java.sql.SQLException e){
                e.printStackTrace();
            }
            return null;

    }


}
