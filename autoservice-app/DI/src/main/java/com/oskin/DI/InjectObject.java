package com.oskin.DI;
import java.lang.reflect.Field;
import com.oskin.Annotations.*;

public class InjectObject {
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
