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
    @Inject
    FunctionsDB functionsDB;
    @Inject
    OrdersByMasterDb ordersByMasterDb;

    public ArrayList<Master> SelectMasters(){
        ArrayList<Master> masters = new ArrayList<>();
        try(Statement statement = connectionDB.getConnection().createStatement()){
            ResultSet setMaster = statement.executeQuery("SELECT * FROM Masters");
            while (setMaster.next()){
                int id = setMaster.getInt("id");
                String name  = setMaster.getString("name");
                Master master = new Master(id, name);
                master.addArrayOrdersId(ordersByMasterDb.selectIdOrdersByMaster(id));
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
                master.addArrayOrdersId(ordersByMasterDb.selectIdOrdersByMaster(id));
                return master;
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Master findMasterInDb(String name){
        String sql = "SELECT * FROM Masters WHERE name = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                int id = set.getInt("id");
                return new Master(id, name);
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public Master findMasterInDb(int id){
        String sql = "SELECT * FROM Masters WHERE id = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                String name = set.getString("name");
                return new Master(id, name);
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteMasterInDB(String name){
        Master master = findMasterInDb(name);
        int id = master.getId();
        boolean inf = functionsDB.deleteInDB(name, NameTables.MASTER);
        if(inf){
            ordersByMasterDb.deleteLinkInDB(id);
        }
        return inf;
    }
    public boolean deleteMasterInDB(int id){
        boolean inf = functionsDB.deleteInDB(id, NameTables.MASTER);
        if(inf){
            ordersByMasterDb.deleteLinkInDB(id);
        }
        return inf;
    }
    public void addMasterInDB(Master master){
        String sql = "INSERT INTO Masters (id, name) VALUES (?, ?)";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, master.getId());
            statement.setString(2, master.getName());
            statement.executeUpdate();
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }
}
