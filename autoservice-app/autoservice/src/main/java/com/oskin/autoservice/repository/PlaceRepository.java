package com.oskin.autoservice.repository;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.SortType;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PlaceRepository implements CrudRepository<Place> {
    private final Logger logger = LoggerFactory.getLogger(PlaceRepository.class);
    Scanner scanner = new Scanner(System.in);

    @Override
    public <G extends SortType> ArrayList<Place> findAll(G sortType) {
        logger.info("Start findAll place ");
        List<Place> places = new ArrayList<>();
        try {
            Query<Place> query = SessionHibernate.getSession().createQuery("FROM Place ORDER by "+sortType.getStringSortType(), Place.class);
            places = query.getResultList();
            logger.info("successful findAll place ");
        } catch (Exception e) {
            logger.error("error findAll place {}", e.getMessage());
        }
        return (ArrayList<Place>) places;
    }

    @Override
    public void create(Place place) {
        logger.info("Start create place");
        Transaction transaction = SessionHibernate.getSession().beginTransaction();
        try {
            SessionHibernate.getSession().persist(place);
            transaction.commit();
            logger.info("successful create place ");
        } catch (Exception e) {
            transaction.rollback();
            logger.error("error create place {}", e.getMessage());
        }
    }

    @Override
    public boolean delete(int id) {
        logger.info("Start delete place ");
        Transaction transaction = SessionHibernate.getSession().beginTransaction();
        try {
            Place place = find(id);
            if (place != null) {
                SessionHibernate.getSession().remove(place);
                logger.info("successful delete place ");
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            logger.error("error delete place {}", e.getMessage());
            transaction.rollback();
        }
        return false;
    }
    public boolean delete(String name) {
        Place place = find(name);
        if (place != null) {
            return delete(place.getId());
        } else {
            return false;
        }
    }
    @Override
    public Place find(int id) {
        logger.info("Start findById place ");
        try {
            Place place = SessionHibernate.getSession().find(Place.class, id);
            if (place != null) {
                logger.info("successful findById place ");
                return place;
            }
        } catch (Exception e) {
            logger.error("error findById {}", e.getMessage());
        }
        logger.info("No found but successful findById place");
        return null;
    }

    @Override
    public void update(Place place) {
        logger.info("Start update place ");
        Transaction transaction = SessionHibernate.getSession().beginTransaction();
        try {
            SessionHibernate.getSession().merge(place);
            logger.info("successful update place ");
            transaction.commit();
        } catch (Exception e) {
            logger.error("error update place {}", e.getMessage());
            transaction.rollback();
        }
    }
    public Place find(String name) {
        logger.info("Start findByName place ");
        try {
            Query<Place> query = SessionHibernate.getSession().createQuery("From Place WHERE name = :name", Place.class);
            query.setParameter("name", name);
            List<Place> places = query.getResultList();
            if (places.size() > 1) {
                while (true) {
                    System.out.println("Было найдено несколько записей. Выберите какую запись выбрать: ");
                    int count = 1;
                    for (Place place : places) {
                        System.out.println(count + ": id:" + place.getId() + " name: " + place.getName());
                        count++;
                    }
                    int x = 0;
                    try {
                        x = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                    }
                    if (x > 0 && x < places.size() + 1) {
                        logger.info("successful findByName with choice place ");
                        return places.get(x - 1);
                    } else {
                        System.out.println("Неправильный ввод");
                    }
                }
            } else if (places.size() == 1) {
                logger.info("successful findByName without choice place");
                return places.get(0);
            }
        } catch (Exception e) {
            logger.error("error findByName place {}", e.getMessage());
        }
        logger.info("No found but successful findByName place");
        return null;
    }
}
