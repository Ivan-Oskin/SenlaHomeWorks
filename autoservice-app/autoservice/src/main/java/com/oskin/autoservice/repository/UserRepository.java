package com.oskin.autoservice.repository;

import com.oskin.autoservice.model.SortType;
import com.oskin.autoservice.model.User;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository implements CrudRepository<User> {
    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final Logger loggerFile = LoggerFactory.getLogger("file");
    SessionHibernate session;

    @Autowired
    UserRepository(SessionHibernate session) {
        this.session = session;
    }

    @Override
    public <G extends SortType> ArrayList<User> findAll(G sortType) {
        logger.info("Start findAll place ");
        List<User> users = new ArrayList<>();
        try {
            Query<User> query = session.getSession().createQuery("FROM User", User.class);
            users = query.getResultList();
            logger.info("successful findAll user ");
        } catch (Exception e) {
            loggerFile.error("error findAll user {}", e.getMessage());
        }
        return (ArrayList<User>) users;
    }

    @Override
    public void create(User user) {
        logger.info("Start create user");
        try {
            session.getSession().merge(user);
            logger.info("successful create user ");
        } catch (Exception e) {
            loggerFile.error("error create user {}", e.getMessage());
        }
    }

    @Override
    public boolean delete(int id) {
        logger.info("Start delete user");
        try {
            User user = find(id);
            if (user != null) {
                session.getSession().remove(user);
                logger.info("successful delete user ");
                return true;
            }
        } catch (Exception e) {
            loggerFile.error("error delete user {}", e.getMessage());
        }
        return false;
    }

    @Override
    public User find(int id) {
        logger.info("Start findById user ");
        try {
            User user = session.getSession().find(User.class, id);
            if (user != null) {
                logger.info("successful findById user");
                return user;
            }
        } catch (Exception e) {
            loggerFile.error("error findById {}", e.getMessage());
        }
        logger.info("No found but successful findById user");
        return null;
    }

    public User findByLogin(String login) {
        logger.info("Start findByLogin user ");
        try {
            Query<User> query = session.getSession().createQuery("From User WHERE login = :login", User.class);
            query.setParameter("login", login);
            List<User> users = query.getResultList();
            if (!users.isEmpty()) {
                return users.get(0);
            }
        } catch (Exception e) {
            loggerFile.error("error findByLogin {}", e.getMessage());
        }
        logger.info("No found but successful findByLogin user");
        return null;
    }

    @Override
    public void update(User user) {
        logger.info("Start update user ");
        try {
            session.getSession().merge(user);
            logger.info("successful update user ");
        } catch (Exception e) {
            loggerFile.error("error update user {}", e.getMessage());
        }
    }
}
