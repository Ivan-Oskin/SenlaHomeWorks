package com.oskin.di;
import com.oskin.annotations.Inject;

import java.lang.reflect.Field;


public class InjectObject {
    public void inject(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Object object = DIContainer.getDependecy(field.getType());
                try {
                    field.set(obj, object);
                } catch (IllegalAccessException e) {
                    System.err.println("произошла ошибка при создании поля");
                }
            }
        }
    }
}
