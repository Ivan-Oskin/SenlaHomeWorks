package com.oskin.DI;

import java.util.HashMap;
import java.util.Map;
import com.oskin.configuration.IConfiguration;

public class DIСontainer {
    private Map<Class, Object> container = new HashMap<>();
    private static DIСontainer instance;
    IBuilder builder;
    IConfiguration config;
    private DIСontainer(IBuilder builder, IConfiguration config){
        this.builder = builder;
        this.config = config;
    };

    public static DIСontainer getInstance(IBuilder builder, IConfiguration config){
        if(instance == null){
            instance = new DIСontainer(builder, config);
        }
        return instance;
    }

    public <T> T getDependecy(Class<T> type){
        if(container.containsKey(type)){
            return (T) container.get(type);
        }

        T obj = builder.create(type);
        System.out.println("Объект создан");
        config.configure(obj);

        Singleton singleton = obj.getClass().getAnnotation(Singleton.class);
        if(singleton != null){
            container.put(obj.getClass(), obj);
            System.out.println("Добавлен в контейнер");
        }
        return obj;
    }
}
