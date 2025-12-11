package com.oskin.DI;

import java.util.HashMap;
import java.util.Map;
import com.oskin.configuration.IConfiguration;

public class DIСontainer {
    private static Map<Class, Object> container = new HashMap<>();
    private static DIСontainer instance;
    private static IBuilder builder;
    private static IInject inject;
    private static IConfiguration config;
    private DIСontainer(IBuilder builder, IConfiguration config, IInject inject){
        this.builder = builder;
        this.config = config;
        this.inject = inject;
    };

    public static DIСontainer getInstance(IBuilder builder, IConfiguration config, IInject inject){
        if(instance == null){
            instance = new DIСontainer(builder, config, inject);
        }
        return instance;
    }

    public static  <T> T getDependecy(Class<T> type){
        if(container.containsKey(type)){
            return (T) container.get(type);
        }
        System.out.println("Получили класс " + type.getName());

        T obj = builder.create(type);
        System.out.println("Создали класс " + type.getName());
        config.configure(obj);
        System.out.println("Конфиг класс " + type.getName());
        inject.inject(obj);
        System.out.println("заинжектил класс " + type.getName());

        Singleton singleton = obj.getClass().getAnnotation(Singleton.class);
        if(singleton != null){
            container.put(obj.getClass(), obj);
        }
        return obj;
    }
}
