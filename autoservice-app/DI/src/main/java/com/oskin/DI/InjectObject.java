package com.oskin.DI;

import com.oskin.configuration.Configuration;

import java.lang.reflect.Field;
import java.util.Dictionary;

public class InjectObject implements IInject {
    @Override
    public void inject(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Object object = DIСontainer.getDependecy(field.getType());
                try{
                    field.set(obj, object);
                } catch (IllegalAccessException e){
                    System.err.println("произошла ошибка при создании поля");
                }
            }
        }
    }
}
