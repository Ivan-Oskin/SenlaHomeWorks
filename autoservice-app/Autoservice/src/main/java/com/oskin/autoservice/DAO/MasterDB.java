package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Master;
import com.oskin.autoservice.View.CarRepairInput;

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
    @Inject
    CarRepairInput carRepairInput;

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
            ArrayList<Master> masters = new ArrayList<>();
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                int id = set.getInt("id");
                Master master = new Master(id, name);
                master.addArrayOrdersId(ordersByMasterDb.selectIdOrdersByMaster(id));
                masters.add(master);
            }
            if(masters.size() > 1){
                while (true){
                    System.out.println("Было найдено несколько записей. Выберите какую запись выбрать: ");
                    int count = 1;
                    for(Master master : masters){
                        System.out.println(count + ": id:" +  master.getId() + " name: "+master.getName());
                        count++;
                    }
                    int x = carRepairInput.inputInt() - 1;
                    if(x > -1 && x < masters.size()){
                        return masters.get(x);
                    }
                    else {
                        System.out.println("Неправильный ввод");
                    }
                }
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
                Master master = new Master(id, name);
                master.addArrayOrdersId(ordersByMasterDb.selectIdOrdersByMaster(id));
                return master;
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteMasterInDB(String name){
        Master master = findMasterInDb(name);
        if(master == null){
            return false;
        } else {
            int id = master.getId();
            boolean inf = functionsDB.deleteInDB(id, NameTables.MASTER);
            if(inf){
                ordersByMasterDb.deleteLinkInDB(id);
            }
            return inf;
        }
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
