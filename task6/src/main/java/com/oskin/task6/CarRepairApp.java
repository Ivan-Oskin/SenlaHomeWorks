package com.oskin.task6;
import com.oskin.task6.View.MainMenu;

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
