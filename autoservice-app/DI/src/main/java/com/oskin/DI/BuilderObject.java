package com.oskin.DI;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Set;

public class BuilderObject {
    public <T> T create(Class<T> type) {
        Class<T> impClass = type;
        if (type.isInterface()) {
            impClass = createObjectImplementsInterface(impClass);
        }
        try {
            Constructor<T> constructor = impClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            System.err.println("Произошла ошибка при создании конструктора класса");
        }
        return null;
    }

    private <T> Class<T> createObjectImplementsInterface(Class<T> imlClass) {
        Reflections scanner = new Reflections("com.myapp");
        Set<? extends Class<?>> candidates = scanner.getSubTypesOf(imlClass);
        if (candidates.isEmpty()) {
            System.err.println("У интерфейса нет наследников");
            return null;
        } else {
            return (Class<T>) candidates.iterator().next();
        }
    }
}
