package com.oskin.autoservice.repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.OrderMaster;

public class SessionHibernate {
    static Logger logger = LoggerFactory.getLogger(SessionHibernate.class);
    private static SessionFactory sessionFactory;
    private static Session session;

    static {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate_cfg.xml").build();
            MetadataSources sources = new MetadataSources(registry);
            sources.addAnnotatedClasses(Master.class);
            sources.addAnnotatedClasses(Order.class);
            sources.addAnnotatedClasses(Place.class);
            sources.addAnnotatedClasses(OrderMaster.class);
            Metadata metadata = sources.getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            logger.error("error start session {}", e.getMessage());
        }
    }

    public static Session getSession() {
        if (session != null && session.isOpen()) {
            return session;
        }
        logger.info("Начало подключения");
        session = sessionFactory.openSession();
        logger.info("Подключение успешно");
        return session;
    }
}
