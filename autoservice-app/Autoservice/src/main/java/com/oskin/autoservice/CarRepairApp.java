package com.oskin.autoservice;
import com.oskin.DI.BuilderObject;
import com.oskin.DI.DIСontainer;
import com.oskin.DI.InjectObject;
import com.oskin.autoservice.View.MainMenu;

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
        DIСontainer DI = new DIСontainer(new BuilderObject(), new InjectObject());
        MainMenu mainMenu = DI.getDependecy(MainMenu.class);
        mainMenu.run();
    }
}
