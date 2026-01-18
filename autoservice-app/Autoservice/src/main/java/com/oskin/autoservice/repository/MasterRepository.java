package com.oskin.autoservice.repository;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Master;
import com.oskin.autoservice.Model.SortType;
import com.oskin.autoservice.Model.SortTypeMaster;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MasterRepository implements CrudRepository<Master> {
    @Inject
    ConnectionDB connectionDB;
    public int inputInt(){
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try{
            input = scanner.nextInt();
            scanner.nextLine();
        }
        catch (InputMismatchException e){
            scanner.nextLine();
            System.err.println("\nНадо ввести только цифру!!!\n");
        }
        return input;
    }
    @Override
    public <G extends SortType> ArrayList<Master> findAll(G sortTypeMaster){
        ArrayList<Master> masters = new ArrayList<>();
        if(!sortTypeMaster.getStringSortType().equals(SortTypeMaster.BUSYNESS.getStringSortType())){
            String sql = "SELECT * FROM masters ORDER BY " + sortTypeMaster.getStringSortType();
            try(PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql)){
                ResultSet setMaster = preparedStatement.executeQuery();
                while (setMaster.next()){
                    int id = setMaster.getInt("id");
                    String name  = setMaster.getString("name");
                    Master master = new Master(id, name);
                    masters.add(master);
                }
            } catch (java.sql.SQLException e){
                e.printStackTrace();
            }
        }
        else {
            try(Statement statement = connectionDB.getConnection().createStatement()) {
                ResultSet setMaster = statement.executeQuery("SELECT masters.id, masters.name FROM masters\n" +
                        "LEFT JOIN order_master ON masters.id = order_master.master_id\n" +
                        "GROUP BY masters.id\n" +
                        "ORDER BY COUNT(order_master) DESC;\n");
                while (setMaster.next()){
                    int id = setMaster.getInt("id");
                    String name  = setMaster.getString("name");
                    Master master = new Master(id, name);
                    masters.add(master);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return masters;
    }
    public Master find(String name){
        String sql = "SELECT * FROM masters WHERE name = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            ArrayList<Master> masters = new ArrayList<>();
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                int id = set.getInt("id");
                Master master = new Master(id, name);
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
                    int x = inputInt() - 1;
                    if(x > -1 && x < masters.size()){
                        return masters.get(x);
                    }
                    else {
                        System.out.println("Неправильный ввод");
                    }
                }
            }else if(masters.size() == 1) {
                return masters.get(0);
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Master find(int id){
        String sql = "SELECT * FROM masters WHERE id = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                String name = set.getString("name");
                Master master = new Master(id, name);
                return master;
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean delete(String name){
        Master master = find(name);
        if(master == null){
            return false;
        } else {
            return delete(master.getId());
        }
    }

    @Override
    public boolean delete(int id){
        String sql = "DELETE FROM masters WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if(result > 0){
                connectionDB.commit();
                return true;
            }
        } catch (java.sql.SQLException e){
            connectionDB.rollback();
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void create(Master master){
        String sql = "INSERT INTO masters (id, name) VALUES (?, ?)";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, master.getId());
            statement.setString(2, master.getName());
            statement.executeUpdate();
            connectionDB.commit();
        } catch (java.sql.SQLException e){
            connectionDB.rollback();
            e.printStackTrace();
        }
    }
    @Override
    public void update(Master master){
        String sql = "UPDATE masters SET name = ? WHERE id = ?";
        try(PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)){
            statement.setString(1, master.getName());
            statement.setInt(2, master.getId());
            statement.executeUpdate();
            connectionDB.commit();
        } catch (SQLException e){
            connectionDB.rollback();
            e.printStackTrace();
        }
    }
}
