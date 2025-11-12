package com.oskin.task5;
import com.oskin.task5.View.MainMenu;

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
    public static void main(String[] args) {
        MainMenu.getInstance().run();
    }
}
