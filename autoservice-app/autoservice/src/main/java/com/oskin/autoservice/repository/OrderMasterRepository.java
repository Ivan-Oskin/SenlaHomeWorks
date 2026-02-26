package com.oskin.autoservice.repository;

import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.autoservice.model.SortType;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class OrderMasterRepository implements CrudRepository<OrderMaster> {
    OrderRepository orderRepository;
    MasterRepository masterRepository;
    SessionHibernate session;

    private final Logger logger = LoggerFactory.getLogger(OrderMasterRepository.class);
    private final Logger loggerFile = LoggerFactory.getLogger("file");

    @Autowired
    public OrderMasterRepository(OrderRepository orderRepository, MasterRepository masterRepository, SessionHibernate session) {
        this.orderRepository = orderRepository;
        this.masterRepository = masterRepository;
        this.session = session;
    }

    @Override
    public <G extends SortType> ArrayList<OrderMaster> findAll(G sortType) {
        logger.info("Start findAll orderMaster ");
        List<OrderMaster> orderMasters = new ArrayList<>();
        try {
            String hql = "SELECT om FROM OrderMaster om " +
                    "LEFT JOIN FETCH om.master m " +
                    "LEFT JOIN FETCH om.order o " +
                    "ORDER BY om." + sortType.getStringSortType();
            Query<OrderMaster> query = session.getSession().createQuery(hql, OrderMaster.class);
            orderMasters = query.getResultList();
            Iterator<OrderMaster> iterator = orderMasters.iterator();
            while (iterator.hasNext()) {
                OrderMaster orderMaster = iterator.next();
                if (orderMaster.getOrder() == null) {
                    String sql = "SELECT order_id FROM order_master WHERE id = " + orderMaster.getId();
                    NativeQuery<Integer> queryPlaceId = session.getSession().createNativeQuery(sql, Integer.class);
                    List<Integer> listPlaceId = queryPlaceId.getResultList();
                    loggerFile.error("order_id = {} не найден", listPlaceId.get(0));
                    iterator.remove();
                }
                if (orderMaster.getMaster() == null) {
                    String sql = "SELECT master_id FROM order_master WHERE id = " + orderMaster.getId();
                    NativeQuery<Integer> queryPlaceId = session.getSession().createNativeQuery(sql, Integer.class);
                    List<Integer> listPlaceId = queryPlaceId.getResultList();
                    loggerFile.error("master_id = {} не найден", listPlaceId.get(0));
                    iterator.remove();
                }
            }
            logger.info("successful findAll order ");
        } catch (Exception e) {
            loggerFile.error("error findAll order {}", e.getMessage());
        }
        return (ArrayList<OrderMaster>) orderMasters;
    }

    @Override

    public void create(OrderMaster orderMaster) {
        logger.info("Start create orderMaster");
        try {
            session.getSession().persist(orderMaster);
            logger.info("successful create orderMaster ");
        } catch (Exception e) {
            loggerFile.error("create orderMaster error {}", e.getMessage());
        }
    }


    public void create(int id, int idMaster, int idOrder) {
        Order order = orderRepository.find(idOrder);
        Master master = masterRepository.find(idMaster);
        OrderMaster orderMaster = new OrderMaster(id, order, master);
        logger.info("Start create orderMaster by fields ");
        try {
            session.getSession().merge(orderMaster);
            logger.info("successful create orderMaster by fields ");
        } catch (Exception e) {
            loggerFile.error("error create orderMaster by fields {}", e.getMessage());
        }
    }

    public ArrayList<OrderMaster> getOrdersByMasterInDB(int idMaster) {
        logger.info("start getOrders orderMaster");
        List<OrderMaster> orderMasters = new ArrayList<>();
        String hql = "SELECT om FROM OrderMaster om " +
                "LEFT JOIN FETCH om.order " +
                "LEFT JOIN FETCH om.master " +
                "WHERE om.master.id = :masterId";
        try {
            Query<OrderMaster> query = session.getSession().createQuery(hql, OrderMaster.class);
            query.setParameter("masterId", idMaster);
            orderMasters = query.getResultList();
            logger.info("successful getOrders orderMaster");
        } catch (Exception e) {
            loggerFile.error("error getOrders orderMaster {}", e.getMessage());
        }
        return (ArrayList<OrderMaster>) orderMasters;
    }


    public void deleteByMaster(int idMaster) {
        logger.info("start deleteByMaster orderMaster");
        String hql = "DELETE FROM OrderMaster om WHERE om.master.id = :masterId";
        try {
            Query<?> query = session.getSession().createQuery(hql);
            query.setParameter("masterId", idMaster);
            int deleted = query.executeUpdate();
            if (deleted > 0) {
                logger.info("successful deleteByMaster orderMaster ");
            }
        } catch (Exception e) {
            loggerFile.error("error deleteByMaster orderMaster {}", e.getMessage());
        }
    }


    public void deleteByOrder(int idOrder) {
        logger.info("start deleteByOrder orderMaster");
        String hql = "DELETE FROM OrderMaster om WHERE om.order.id = :orderId";
        try {
            Query<?> query = session.getSession().createQuery(hql);
            query.setParameter("orderId", idOrder);
            int deleted = query.executeUpdate();
            if (deleted > 0) {
                logger.info("successful deleteByOrder orderMaster ");
            }
        } catch (Exception e) {
            loggerFile.error("error deleteByOrder orderMaster {}", e.getMessage());
        }
    }

    @Override

    public boolean delete(int id) {
        logger.info("Start delete orderMaster ");
        try {
            OrderMaster orderMaster = find(id);
            if (orderMaster != null) {
                session.getSession().remove(orderMaster);
                logger.info("successful delete orderMaster");
                return true;
            }
        } catch (Exception e) {
            loggerFile.error("error delete orderMaster {}", e.getMessage());
        }
        return false;
    }

    public ArrayList<OrderMaster> getMastersByOrderInDB(int idOrder) {
        logger.info("start getMaster orderMaster");
        List<OrderMaster> orderMasterList = new ArrayList<>();
        String hql = "SELECT om FROM OrderMaster om " +
                "LEFT JOIN FETCH om.order " +
                "LEFT JOIN FETCH om.master " +
                "WHERE om.order.id = :orderId";
        try {
            Query<OrderMaster> query = session.getSession().createQuery(hql, OrderMaster.class);
            query.setParameter("orderId", idOrder);
            orderMasterList = query.getResultList();
            logger.info("successful getMasters orderMaster");
        } catch (Exception e) {
            loggerFile.error("error getMasters orderMaster {}", e.getMessage());
        }
        return (ArrayList<OrderMaster>) orderMasterList;
    }

    @Override
    public OrderMaster find(int id) {
        logger.info("Start findById orderMaster");
        try {
            OrderMaster orderMaster = session.getSession().find(OrderMaster.class, id);
            if (orderMaster != null) {
                logger.info("successful findById orderMaster ");
                return orderMaster;
            }
        } catch (Exception e) {
            loggerFile.error("error findById orderMaster {}", e.getMessage());
        }
        logger.info("No found but successful findById orderMaster");
        return null;
    }

    @Override

    public void update(OrderMaster orderMaster) {
        logger.info("Start update orderMaster ");
        try {
            session.getSession().merge(orderMaster);
            logger.info("successful update orderMaster ");
        } catch (Exception e) {
            loggerFile.error("error update orderMaster {}", e.getMessage());
        }
    }
}
