package com.oskin.autoservice.repository;

import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.SortType;
import com.oskin.autoservice.model.SortTypeMaster;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Repository
public class MasterRepository implements CrudRepository<Master> {
    private final Logger logger = LoggerFactory.getLogger(MasterRepository.class);
    private final Logger loggerFile = LoggerFactory.getLogger("file");

    SessionHibernate session;
    @Autowired
    MasterRepository(SessionHibernate session) {
        this.session = session;
    }

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
        List<Master> masters = new ArrayList<>();
        if (!sortTypeMaster.getStringSortType().equals(SortTypeMaster.BUSYNESS.getStringSortType())) {
            try {
                Query<Master> query = session.getSession().createQuery("FROM Master ORDER by " + sortTypeMaster.getStringSortType(), Master.class);
                masters = query.getResultList();
                logger.info("successful findAll master ");
            } catch (Exception e) {
                loggerFile.error("error findAll master {}", e.getMessage());
            }
        } else {
            String hql = "SELECT m.id, m.name FROM Master m " +
                    "LEFT JOIN OrderMaster om WITH om.master = m " +
                    "GROUP BY m.id " +
                    "ORDER BY COUNT(om) DESC ";
            try {
                Query<Master> query = session.getSession().createQuery(hql, Master.class);
                masters = query.getResultList();
                logger.info("successful findAll master and order by count orders");
            } catch (Exception e) {
                loggerFile.error("findAll master and order by count orders error {}", e.getMessage());
            }
        }
        return (ArrayList<Master>) masters;
    }

    public Master find(String name) {
        logger.info("start findByName master");
        try {
            Query<Master> query = session.getSession().createQuery("From Master WHERE name = :name", Master.class);
            query.setParameter("name", name);
            List<Master> masters = query.getResultList();
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
        } catch (Exception e) {
            loggerFile.error("error findByName master {}", e.getMessage());
        }
        logger.info("No found but successful findByName master");
        return null;
    }

    @Override
    public Master find(int id) {
        logger.info("Start findById master");
        try {
            Master master = session.getSession().find(Master.class, id);
            if (master != null) {
                logger.info("successful findById master ");
                return master;
            }
        } catch (Exception e) {
            loggerFile.error("error findById {}", e.getMessage());
        }
        logger.info("No found but successful findById master");
        return null;
    }

    @Override
    public boolean delete(int id) {
        logger.info("Start delete master ");
        try {
            Master master = find(id);
            if (master != null) {
                session.getSession().remove(master);
                logger.info("successful delete master");
                return true;
            }
        } catch (Exception e) {
            loggerFile.error("error delete master {}", e.getMessage());
        }
        return false;
    }

    @Override
    public void create(Master master) {
        logger.info("Start create master");
        try {
            session.getSession().merge(master);
            logger.info("successful create master ");
        } catch (Exception e) {
            loggerFile.error("error create master {}", e.getMessage());
        }
    }

    @Override
    public void update(Master master) {
        logger.info("Start update master ");
        try {
            session.getSession().merge(master);
            logger.info("successful update master ");
        } catch (Exception e) {
            loggerFile.error("error update master {}", e.getMessage());
        }
    }
}
