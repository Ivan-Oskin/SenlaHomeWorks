package com.oskin.autoservice.DAO;

import com.oskin.Annotations.Inject;
import java.sql.PreparedStatement;

public class FunctionsDB {
    @Inject
    ConnectionDB connectionDB;
    public boolean deleteInDB(int id, NameTables nameTable){
        String sql = "DELETE FROM "+nameTable.getNAME_TABLE()+" WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if(result > 0){
                return true;
            }
        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}

