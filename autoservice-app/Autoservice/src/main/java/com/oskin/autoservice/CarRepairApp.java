package com.oskin.autoservice;
import com.oskin.DI.BuilderObject;
import com.oskin.DI.DIСontainer;
import com.oskin.DI.InjectObject;
import com.oskin.autoservice.View.MainMenu;
import com.oskin.configuration.Configuration;

public class CarRepairApp {
    private static CarRepairApp instance;

    private CarRepairApp() {
    }

    public static CarRepairApp getInstance() {
        if (instance == null) {
            instance = new CarRepairApp();
        }
        return instance;
    }
    public static void main(String[] args)
    {
        DIСontainer.getInstance(new BuilderObject(), new Configuration(), new InjectObject());
        System.out.println("Помещаем класс маин меню");
        MainMenu mainMenu = DIСontainer.getDependecy(MainMenu.class);
        mainMenu.run();
    }
}
