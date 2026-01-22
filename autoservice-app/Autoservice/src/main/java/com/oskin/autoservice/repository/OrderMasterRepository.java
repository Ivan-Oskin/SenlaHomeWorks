package com.oskin.autoservice.repository;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Master;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.OrderMaster;
import com.oskin.autoservice.Model.SortType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderMasterRepository implements CrudRepository<OrderMaster> {
    @Inject
    ConnectionDB connectionDB;
    @Inject
    OrderRepository orderRepository;
    @Inject
    MasterRepository masterRepository;

    @Override
    public <G extends SortType> ArrayList<OrderMaster> findAll(G sortType) {
        ArrayList<OrderMaster> orderMaster = new ArrayList<>();
        String sql = "SELECT * FROM order_master ORDER BY " + sortType.getStringSortType();
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            ResultSet setOrderMaster = statement.executeQuery();
            while (setOrderMaster.next()) {
                int id = setOrderMaster.getInt("id");
                Order order = orderRepository.find(setOrderMaster.getInt("order_id"));
                Master master = masterRepository.find(setOrderMaster.getInt("master_id"));
                orderMaster.add(new OrderMaster(id, order, master));
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return orderMaster;
    }

    @Override
    public void create(OrderMaster orderMaster) {
        String sql = "INSERT INTO order_master (id, master_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, orderMaster.getId());
            statement.setInt(2, orderMaster.getMaster().getId());
            statement.setInt(3, orderMaster.getOrder().getId());
            statement.executeUpdate();
            connectionDB.commit();
        } catch (java.sql.SQLException e) {
            connectionDB.rollback();
            e.printStackTrace();
        }
    }

    public void create(int id, int idMaster, int idOrder) {
        String sql = "INSERT INTO order_master (id, master_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, idMaster);
            statement.setInt(3, idOrder);
            statement.executeUpdate();
            connectionDB.commit();
        } catch (java.sql.SQLException e) {
            connectionDB.rollback();
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
                Order order = orderRepository.find(setOrderMaster.getInt("order_id"));
                Master master = masterRepository.find(setOrderMaster.getInt("master_id"));
                OrderMaster orderMaster = new OrderMaster(id, order, master);
                orderMasterArrayList.add(orderMaster);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return orderMasterArrayList;
    }

    public boolean deleteByMaster(int idMaster) {
        String sql = "DELETE FROM order_master WHERE master_id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idMaster);
            int x = statement.executeUpdate();
            if (x > 0) {
                return true;
            }
            connectionDB.commit();
        } catch (java.sql.SQLException e) {
            connectionDB.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM order_master WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int x = statement.executeUpdate();
            if (x > 0) {
                connectionDB.commit();
                return true;
            }
        } catch (java.sql.SQLException e) {
            connectionDB.rollback();
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<OrderMaster> getMastersByOrderInDB(int idOrder) {
        ArrayList<OrderMaster> orderMasterArrayList = new ArrayList<>();
        String sql = "SELECT * FROM order_master WHERE order_id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idOrder);
            ResultSet setOrderMaster = statement.executeQuery();
            while (setOrderMaster.next()) {
                int id = setOrderMaster.getInt("id");
                Order order = orderRepository.find(setOrderMaster.getInt("order_id"));
                Master master = masterRepository.find(setOrderMaster.getInt("master_id"));
                OrderMaster orderMaster = new OrderMaster(id, order, master);
                orderMasterArrayList.add(orderMaster);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return orderMasterArrayList;
    }

    @Override
    public OrderMaster find(int id) {
        String sql = "SELECT * FROM order_master WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int master_id = set.getInt("master_id");
                int order_id = set.getInt("order_id");
                Master master = masterRepository.find(master_id);
                Order order = orderRepository.find(order_id);
                return new OrderMaster(id, order, master);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(OrderMaster orderMaster) {
        String sql = "UPDATE order_master SET master_id = ?, order_id = ? WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, orderMaster.getMaster().getId());
            statement.setInt(2, orderMaster.getOrder().getId());
            statement.setInt(3, orderMaster.getId());
            statement.executeUpdate();
            connectionDB.commit();
        } catch (SQLException e) {
            connectionDB.rollback();
            e.printStackTrace();
        }
    }
}
