package com.oskin.autoservice.repository;

import com.oskin.autoservice.model.IIndentified;
import com.oskin.autoservice.model.SortType;

import java.util.ArrayList;

public interface CRUDHibernateRepository <T extends IIndentified> {

    void create(T object);

    void update(T object);

    boolean delete(int id);

    <G extends SortType> ArrayList<T> findAll(G sortType);

    T find(int id);
}
