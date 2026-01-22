package com.oskin.autoservice.repository;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Master;
import com.oskin.autoservice.Model.Order;
import com.oskin.autoservice.Model.OrderMaster;
import com.oskin.autoservice.Model.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Logger logger = LoggerFactory.getLogger(OrderMasterRepository.class);

    @Override
    public <G extends SortType> ArrayList<OrderMaster> findAll(G sortType) {
        logger.info("start findAll orderMaster");
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
            logger.info("successful findAll orderMaster");
        } catch (java.sql.SQLException e) {
            logger.error("error findAll orderMaster {}", e.getMessage());
        }
        return orderMaster;
    }
    @Override
    public void create(OrderMaster orderMaster) {
        logger.info("start create orderMaster by object");
        String sql = "INSERT INTO order_master (id, master_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, orderMaster.getId());
            statement.setInt(2, orderMaster.getMaster().getId());
            statement.setInt(3, orderMaster.getOrder().getId());
            statement.executeUpdate();
            logger.info("successful create orderMaster by object");
            connectionDB.commit();
        } catch (java.sql.SQLException e) {
            connectionDB.rollback();
            logger.error("error create orderMaster by object {}", e.getMessage());
        }
    }

    public void create(int id, int idMaster, int idOrder) {
        logger.info("start create orderMaster by fields");
        String sql = "INSERT INTO order_master (id, master_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setInt(2, idMaster);
            statement.setInt(3, idOrder);
            statement.executeUpdate();
            logger.info("successful create orderMaster by fields");
            connectionDB.commit();
        } catch (java.sql.SQLException e) {
            connectionDB.rollback();
            logger.error("error create orderMaster by fields {}", e.getMessage());
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
        logger.info("start getOrders orderMaster");
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
            logger.info("successful getOrders orderMaster");
        } catch (java.sql.SQLException e) {
            logger.error("error getOrders orderMaster {}", e.getMessage());
        }
        return orderMasterArrayList;
    }

    public boolean deleteByMaster(int idMaster) {
        logger.info("start deleteByMaster orderMaster");
        String sql = "DELETE FROM order_master WHERE master_id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idMaster);
            int x = statement.executeUpdate();
            if (x > 0) {
                logger.info("successful deleteByMaster orderMaster");
                connectionDB.commit();
                return true;
            }
        } catch (java.sql.SQLException e) {
            logger.error("error deleteByMaster orderMaster {}", e.getMessage());
            connectionDB.rollback();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        logger.info("start delete orderMaster");
        String sql = "DELETE FROM order_master WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int x = statement.executeUpdate();
            if (x > 0) {
                logger.info("successful delete orderMaster");
                connectionDB.commit();
                return true;
            }
        } catch (java.sql.SQLException e) {
            logger.error("error delete orderMaster {}", e.getMessage());
            connectionDB.rollback();

        }
        return false;
    }

    public ArrayList<OrderMaster> getMastersByOrderInDB(int idOrder) {
        logger.info("start getMaster orderMaster");
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
            logger.info("successful getMasters orderMaster");
        } catch (java.sql.SQLException e) {
            logger.error("error getMasters orderMaster {}", e.getMessage());
        }
        return orderMasterArrayList;
    }

    @Override
    public OrderMaster find(int id) {
        logger.info("start findById orderMaster");
        String sql = "SELECT * FROM order_master WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int master_id = set.getInt("master_id");
                int order_id = set.getInt("order_id");
                Master master = masterRepository.find(master_id);
                Order order = orderRepository.find(order_id);
                logger.info("successful find orderMaster");
                return new OrderMaster(id, order, master);
            }
        } catch (java.sql.SQLException e) {
            logger.error("error find orderMaster {}", e.getMessage());
        }
        logger.info("No found but successful findById orderMaster");
        return null;
    }

    @Override
    public void update(OrderMaster orderMaster) {
        logger.info("start update orderMaster");
        String sql = "UPDATE order_master SET master_id = ?, order_id = ? WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, orderMaster.getMaster().getId());
            statement.setInt(2, orderMaster.getOrder().getId());
            statement.setInt(3, orderMaster.getId());
            statement.executeUpdate();
            logger.info("successful update orderMaster");
            connectionDB.commit();
        } catch (SQLException e) {
            connectionDB.rollback();
            logger.error("error update orderMaster {}", e.getMessage());
        }
    }
}
