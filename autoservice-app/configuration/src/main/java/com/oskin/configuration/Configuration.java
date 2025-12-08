package com.oskin.configuration;

import javax.swing.table.TableRowSorter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Properties;

public class Configuration {

    public Configuration(){};

    private Properties properties = new Properties();
    private void injectProperty(String path){
        try(InputStream stream = new FileInputStream(path)){
            properties.load(stream);
        } catch (IOException e){
            System.err.println("Произошла ошибка с файлом Property");
        }
    }

    private Object convert(String value, Class<?> fieldType){
        if(fieldType == boolean.class) return Boolean.parseBoolean(value);
        if(fieldType == int.class) return  Integer.parseInt(value);
        if(fieldType == double.class) return Double.parseDouble(value);
        if(fieldType == float.class) return Float.parseFloat(value);
        return value;
    }

    public void configure(Object obj){
        for(Field field : obj.getClass().getDeclaredFields()){
            ConfigProperty config = field.getAnnotation(ConfigProperty.class);
            if(config != null){
                injectProperty(config.configFileName());
                String key = config.propertyName().isEmpty() ? obj.getClass().getSimpleName().toUpperCase() + "." +
                        field.getName().toUpperCase() : config.propertyName();
                String value = properties.getProperty(key);
                if(value == null) return;
                field.setAccessible(true);
                try {
                    field.set(obj, value);
                    field.set(obj, value);
                } catch (java.lang.IllegalAccessException e){
                    System.err.println("Произошла ошибка при извлечения значения в поле из конфигурации");
                }
            }
        }
    }
}
