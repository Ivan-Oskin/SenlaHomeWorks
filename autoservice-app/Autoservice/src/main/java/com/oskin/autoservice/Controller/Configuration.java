package com.oskin.autoservice.Controller;
import com.oskin.autoservice.View.CarRepairInput;
import java.io.*;
import java.util.Properties;

public class Configuration {
    private static Configuration instance;
    private Properties properties = new Properties();
    File file = new File("configuration.properties");
    private boolean placeAdd = true;
    private boolean placeDelete = true;
    private boolean orderOffset = true;
    private boolean orderDelete = true;

    public Configuration(){
    }

    public static Configuration getInstance(){
        if(instance == null){
            instance = new Configuration();
        }
        return instance;
    }

    public void loadConfiguration(){
        try{
            properties.load(new FileReader(file));
            placeAdd = Boolean.parseBoolean(properties.getProperty("place.add"));
            placeDelete = Boolean.parseBoolean(properties.getProperty("place.delete"));
            orderOffset = Boolean.parseBoolean(properties.getProperty("order.offset"));
            orderDelete = Boolean.parseBoolean(properties.getProperty("order.delete"));
        }
        catch (IOException ex){
            System.err.println("Произошла ошибка с файлом конфигурации. Будет создан новый файл конфигурации");
            CreateProperties();
        }
    }

    private void CreateProperties(){
        properties.setProperty("place.add", Boolean.toString(placeAdd));
        properties.setProperty("place.delete", Boolean.toString(placeDelete));
        properties.setProperty("order.offset", Boolean.toString(orderOffset));
        properties.setProperty("order.delete", Boolean.toString(orderDelete));
        try(FileOutputStream output = new FileOutputStream("configuration.properties")) {
            properties.store(output, null);
        } catch (IOException e){
            System.err.println("Произошла ошибка при сохранении файла");
        }
    }
    public boolean getRulePlaceAdd(){
        return placeAdd;
    }
    public boolean getRulePlaceDelete(){
        return placeDelete;
    }
    public boolean getRuleOrderOffset(){
        return orderOffset;
    }
    public boolean getRuleOrderDelete(){
        return orderDelete;
    }

    public void toggle(){
        boolean change = false;
        while (true){
            System.out.println("1. Добавление свободных мест - " + (placeAdd?"Разрешено":"Запрещено"));
            System.out.println("2. Удаление свободных мест - " + (placeDelete?"Разрешено":"Запрещено"));
            System.out.println("3. Смещение заказов - " + (orderOffset?"Разрешено":"Запрещено"));
            System.out.println("4 .Удаление заказов - " + (orderDelete?"Разрешено":"Запрещено"));
            System.out.println("Введите цифру для изменения или 0 для сохранения и выхода");
            int x = CarRepairInput.getInstance().inputInt();
            if(x > 0 && x < 5) change = true;
            switch (x){
                case 0:{
                    if(change){
                        try(FileOutputStream output = new FileOutputStream("configuration.properties")) {
                            properties.store(output, null);
                        } catch (IOException e){
                            System.err.println("Произошла ошибка при сохранении файла");
                        }
                    }
                    return;
                }
                case 1:{
                    if(placeAdd) this.placeAdd = false;
                    else this.placeAdd = true;
                    properties.setProperty("place.add", Boolean.toString(placeAdd));
                    break;
                }
                case 2:{
                    if(placeDelete) this.placeDelete = false;
                    else this.placeDelete = true;
                    properties.setProperty("place.delete", Boolean.toString(placeDelete));
                    break;
                }
                case 3:{
                    if(orderOffset) this.orderOffset = false;
                    else this.orderOffset = true;
                    properties.setProperty("order.offset", Boolean.toString(orderOffset));
                    break;
                }case 4:{
                    if(orderDelete) this.orderDelete = false;
                    else this.orderDelete = true;
                    properties.setProperty("order.delete", Boolean.toString(orderDelete));
                    break;
                }
                default:
                    System.out.println("Неправильный ввод");
                    break;
            }
        }
    }
}
