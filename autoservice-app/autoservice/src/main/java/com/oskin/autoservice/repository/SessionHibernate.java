package com.oskin.autoservice.repository;
import com.oskin.config.HibernateConfig;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SessionHibernate {
    private final static Logger logger = LoggerFactory.getLogger(SessionHibernate.class);
    private final static Logger loggerFile = LoggerFactory.getLogger("file");
    HibernateConfig hibernateConfig;

    @Autowired
    SessionHibernate(HibernateConfig hibernateConfig) {
        this.hibernateConfig = hibernateConfig;
    }

    public Session getSession() {
        try{
            logger.info("Начало подключения");
            Session session = hibernateConfig.getSessionFactory().getCurrentSession();
            logger.info("Подключение успешно");
            return session;
        } catch (Exception e) {
            loggerFile.info("error connection - {}", e.getMessage());
            return hibernateConfig.getSessionFactory().openSession();
        }
    }
}
