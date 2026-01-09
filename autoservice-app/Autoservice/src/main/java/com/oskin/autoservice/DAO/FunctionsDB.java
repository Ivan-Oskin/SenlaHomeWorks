package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FunctionsDB {
    @Inject
    ConnectionDB connectionDB;
    public boolean deleteInDB(int id, NameTables nameTable){
        String sql = "DELETE FROM "+nameTable.getNAME_TABLE()+" WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if(result > 0){
                commit();
                return true;
            }
        } catch (java.sql.SQLException e){
            rollback();
            e.printStackTrace();
        }
        return false;
    }

    public void commit(){
        try {
            connectionDB.getConnection().commit();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void rollback(){
        try {
            connectionDB.getConnection().rollback();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}

