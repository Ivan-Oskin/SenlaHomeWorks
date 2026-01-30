package com.oskin.autoservice.repository;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.model.SortType;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;

public class OrderRepository implements CrudRepository<Order> {
    Scanner scanner = new Scanner(System.in);
    private final Logger logger = LoggerFactory.getLogger(OrderRepository.class);
    private final Logger loggerFile = LoggerFactory.getLogger("file");

    @Override
    public <G extends SortType> ArrayList<Order> findAll(G sortType) {
        logger.info("Start findAll order ");
        List<Order> orders = new ArrayList<>();
        try {
            String hql = "SELECT o FROM Order o " +
                    "LEFT JOIN FETCH o.place p " +
                    "ORDER BY o." + sortType.getStringSortType();
            Query<Order> query = SessionHibernate.getSession().createQuery(hql, Order.class);
            orders = query.getResultList();
            Iterator<Order> iterator = orders.iterator();
            while (iterator.hasNext()) {
                Order order = iterator.next();
                 if (order.getPlace() == null) {
                    String sql = "SELECT place_id FROM orders WHERE id = " + order.getId();
                    NativeQuery<Integer> queryPlaceId = SessionHibernate.getSession().createNativeQuery(sql, Integer.class);
                    List<Integer> listPlaceId = queryPlaceId.getResultList();
                    loggerFile.error("place_id = {} не найден, order {} не будет выведен", listPlaceId.get(0), order.getName());
                    iterator.remove();
                }
            }
            logger.info("successful findAll order ");
        } catch (Exception e) {
            loggerFile.error("error findAll order {}", e.getMessage());
        }
        return (ArrayList<Order>) orders;
    }

    public Order find(String name) {
        logger.info("start findByName order");
        try {
            Query<Order> query = SessionHibernate.getSession().createQuery("From Order WHERE name = :name", Order.class);
            query.setParameter("name", name);
            List<Order> orders = query.getResultList();
            if (orders.size() > 1) {
                while (true) {
                    System.out.println("Было найдено несколько записей. Выберите какую запись выбрать: ");
                    int count = 1;
                    for (Order order : orders) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
                        System.out.println(count + ": id:" + order.getId() + " name: " + order.getName() + " Create Time: " + order.getTimeCreate().format(formatter));
                        count++;
                    }
                    int x = 0;
                    try {
                        x = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                    }
                    if (x > 0 && x < orders.size() + 1) {
                        logger.info("successful findByName with choice order");
                        return orders.get(x - 1);
                    } else {
                        System.out.println("Неправильный ввод");
                    }
                }
            } else if (orders.size() == 1) {
                logger.info("successful findByName without choice order");
                return orders.get(0);
            }
        } catch (Exception e) {
            loggerFile.error("error findByName order {}", e.getMessage());
        }
        logger.info("No found but successful findByName order");
        return null;
    }

    @Override
    public Order find(int id) {
        logger.info("Start findById order ");
        try {
            Order order = SessionHibernate.getSession().find(Order.class, id);
            if (order != null) {
                logger.info("successful findById order ");
                return order;
            }
        } catch (Exception e) {
            loggerFile.error("error findById {}", e.getMessage());
        }
        logger.info("No found but successful findById order");
        return null;
    }

    public boolean delete(String name) {
        Order order = find(name);
        if (order == null) {
            return false;
        } else {
            return delete(order.getId());
        }
    }

    @Override
    public boolean delete(int id) {
        logger.info("Start delete order ");
        Transaction transaction = SessionHibernate.getSession().beginTransaction();
        try {
            Order order = find(id);
            if (order != null) {
                SessionHibernate.getSession().remove(order);
                logger.info("successful delete order ");
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            loggerFile.error("error delete order {}", e.getMessage());
            transaction.rollback();
        }
        return false;
    }

    @Override
    public void create(Order order) {
        logger.info("Start create order");
        Transaction transaction = SessionHibernate.getSession().beginTransaction();
        try {
            SessionHibernate.getSession().merge(order);
            transaction.commit();
            logger.info("successful create order");
        } catch (Exception e) {
            transaction.rollback();
            loggerFile.error("error create order {}", e.getMessage());
        }
    }

    public boolean changeStatusInDb(String name, StatusOrder statusOrder) {
        logger.info("start changeStatus order");
        String hql = "UPDATE Order o SET o.status = :status WHERE o.name = :name;";
        Transaction transaction = SessionHibernate.getSession().beginTransaction();
        try {
            Query<?> query = SessionHibernate.getSession().createQuery(hql);
            query.setParameter("status", statusOrder);
            query.setParameter("name", name);
            int changed = query.executeUpdate();
            if (changed > 0) {
                logger.info("successful changeStatus order");
                transaction.commit();
                if (SessionHibernate.getSession().getSessionFactory().getCache() != null) {
                    SessionHibernate.getSession().clear();
                }
                return true;
            }
        } catch (Exception e) {
            loggerFile.error("error changeStatus order {}", e.getMessage());
            transaction.rollback();
        }
        return false;
    }

    public boolean offsetInDb(String name, LocalDateTime timeStart, LocalDateTime timeComplete) {
        logger.info("start offset order");
        String hql = "UPDATE Order o SET o.timeStart = :timeStart, timeComplete = :timeComplete WHERE name = :name";
        Transaction transaction = SessionHibernate.getSession().beginTransaction();
        try {
            Query<?> query = SessionHibernate.getSession().createQuery(hql);
            query.setParameter("timeStart", timeStart);
            query.setParameter("timeComplete", timeComplete);
            query.setParameter("name", name);
            int changed = query.executeUpdate();
            if (changed > 0) {
                logger.info("successful offset order");
                transaction.commit();
                if (SessionHibernate.getSession().getSessionFactory().getCache() != null) {
                    SessionHibernate.getSession().clear();
                }
                return true;
            }
        } catch (Exception e) {
            loggerFile.error("error offset order {}", e.getMessage());
            transaction.rollback();
        }
        return false;
    }

    public void update(Order order) {
        logger.info("Start update order ");
        Transaction transaction = SessionHibernate.getSession().beginTransaction();
        try {
            SessionHibernate.getSession().merge(order);
            logger.info("successful update order ");
            transaction.commit();
        } catch (Exception e) {
            loggerFile.error("error update order {}", e.getMessage());
            transaction.rollback();
        }
    }
}
