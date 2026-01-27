package com.oskin.di;
import com.oskin.annotations.Singleton;

import java.util.HashMap;
import java.util.Map;
public class DIContainer {
    private static Map<Class, Object> container = new HashMap<>();
    private static DIContainer instance;
    private static BuilderObject builder;
    private static InjectObject inject;
    public DIContainer(BuilderObject builder, InjectObject inject) {
        this.builder = builder;
        this.inject = inject;
    }

    public static  <T> T getDependecy(Class<T> type) {
        if (container.containsKey(type)) {
            return (T) container.get(type);
        }

        T obj = builder.create(type);
        inject.inject(obj);

        Singleton singleton = obj.getClass().getAnnotation(Singleton.class);
        if (singleton != null) {
            container.put(obj.getClass(), obj);
        }
        return obj;
    }
}
