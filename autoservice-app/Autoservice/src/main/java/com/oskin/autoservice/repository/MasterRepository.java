package com.oskin.autoservice.repository;

import com.oskin.Annotations.Inject;
import com.oskin.autoservice.Model.Master;
import com.oskin.autoservice.Model.SortType;
import com.oskin.autoservice.Model.SortTypeMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Logger logger = LoggerFactory.getLogger(MasterRepository.class);

    public int inputInt() {
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try {
            input = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.err.println("\nНадо ввести только цифру!!!\n");
        }
        return input;
    }

    @Override
    public <G extends SortType> ArrayList<Master> findAll(G sortTypeMaster) {
        logger.info("start findAll master");
        ArrayList<Master> masters = new ArrayList<>();
        if (!sortTypeMaster.getStringSortType().equals(SortTypeMaster.BUSYNESS.getStringSortType())) {
            String sql = "SELECT * FROM masters ORDER BY " + sortTypeMaster.getStringSortType();
            try (PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql)) {
                ResultSet setMaster = preparedStatement.executeQuery();
                while (setMaster.next()) {
                    int id = setMaster.getInt("id");
                    String name = setMaster.getString("name");
                    Master master = new Master(id, name);
                    masters.add(master);
                }
                logger.info("successful find master and order by fields");
            } catch (java.sql.SQLException e) {
                logger.error("error findAll master and order by fields {}", e.getMessage());
            }
        } else {
            try (Statement statement = connectionDB.getConnection().createStatement()) {
                ResultSet setMaster = statement.executeQuery("SELECT masters.id, masters.name FROM masters\n" +
                        "LEFT JOIN order_master ON masters.id = order_master.master_id\n" +
                        "GROUP BY masters.id\n" +
                        "ORDER BY COUNT(order_master) DESC;\n");
                while (setMaster.next()) {
                    int id = setMaster.getInt("id");
                    String name = setMaster.getString("name");
                    Master master = new Master(id, name);
                    masters.add(master);
                }
                logger.info("successful findAll master and order by count orders");
            } catch (SQLException e) {
                logger.error("error findAll master and order by count orders {}", e.getMessage());
            }
        }
        return masters;
    }

    public Master find(String name) {
        logger.info("start findByName master");
        String sql = "SELECT * FROM masters WHERE name = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            ArrayList<Master> masters = new ArrayList<>();
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int id = set.getInt("id");
                Master master = new Master(id, name);
                masters.add(master);
            }
            if (masters.size() > 1) {
                while (true) {
                    System.out.println("Было найдено несколько записей. Выберите какую запись выбрать: ");
                    int count = 1;
                    for (Master master : masters) {
                        System.out.println(count + ": id:" + master.getId() + " name: " + master.getName());
                        count++;
                    }
                    int x = inputInt() - 1;
                    if (x > -1 && x < masters.size()) {
                        logger.info("successful findByName master with choices");
                        return masters.get(x);
                    } else {
                        System.out.println("Неправильный ввод");
                    }
                }
            } else if (masters.size() == 1) {
                logger.info("successful findByName master without choices");
                return masters.get(0);
            }
        } catch (java.sql.SQLException e) {
            logger.error("error findByName master {}", e.getMessage());
        }
        logger.info("No found but successful findByName master");
        return null;
    }

    @Override
    public Master find(int id) {
        logger.info("start findById master");
        String sql = "SELECT * FROM masters WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                String name = set.getString("name");
                Master master = new Master(id, name);
                return master;
            }
            logger.info("successful findById master");
        } catch (java.sql.SQLException e) {
            logger.error("error findById master {}", e.getMessage());
        }
        logger.info("No found but successful findById master");
        return null;
    }

    public boolean delete(String name) {
        Master master = find(name);
        if (master == null) {
            return false;
        } else {
            return delete(master.getId());
        }
    }

    @Override
    public boolean delete(int id) {
        logger.info("start delete master");
        String sql = "DELETE FROM masters WHERE id = ?;";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if (result > 0) {
                logger.info("successful delete master");
                connectionDB.commit();
                return true;
            }
        } catch (java.sql.SQLException e) {
            logger.error("error delete master {}", e.getMessage());
            connectionDB.rollback();
        }
        return false;
    }

    @Override
    public void create(Master master) {
        logger.info("start create master");
        String sql = "INSERT INTO masters (id, name) VALUES (?, ?)";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, master.getId());
            statement.setString(2, master.getName());
            statement.executeUpdate();
            logger.info("successful create master");
            connectionDB.commit();
        } catch (java.sql.SQLException e) {
            connectionDB.rollback();
            logger.error("error create master {}", e.getMessage());
        }
    }

    @Override
    public void update(Master master) {
        logger.info("start update master");
        String sql = "UPDATE masters SET name = ? WHERE id = ?";
        try (PreparedStatement statement = connectionDB.getConnection().prepareStatement(sql)) {
            statement.setString(1, master.getName());
            statement.setInt(2, master.getId());
            statement.executeUpdate();
            logger.info("successful update master");
            connectionDB.commit();
        } catch (SQLException e) {
            logger.error("error update master {}", e.getMessage());
            connectionDB.rollback();
        }
    }
}
