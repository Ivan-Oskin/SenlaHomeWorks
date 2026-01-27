package com.oskin.autoservice;

import com.oskin.di.BuilderObject;
import com.oskin.di.DIContainer;
import com.oskin.di.InjectObject;
import com.oskin.autoservice.view.MainMenu;

public final class CarRepairApp {
    private static CarRepairApp instance;

    private CarRepairApp() {
    }

    public static CarRepairApp getInstance() {
        if (instance == null) {
            instance = new CarRepairApp();
        }
        return instance;
    }

    public static void main(String[] args) {
        DIContainer DI = new DIContainer(new BuilderObject(), new InjectObject());
        MainMenu mainMenu = DI.getDependecy(MainMenu.class);
        mainMenu.run();
    }
}
