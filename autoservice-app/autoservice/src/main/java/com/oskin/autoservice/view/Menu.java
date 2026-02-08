package com.oskin.autoservice.view;

import java.util.ArrayList;

public class Menu {
    private final String title;
    private final ArrayList<MenuItem> menuItems;

    public void executeOfNumber(int number) {
        if (number >= 0 && number < menuItems.size()) {
            menuItems.get(number).execute();
        } else {
            System.out.print("Команда не найдена");
        }
    }

    public void getInformation() {
        System.out.println(title);
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.print((i + 1) + ". " + menuItems.get(i).getTitle() + " ");
            if (i == 5) System.out.print("\n");
        }
        System.out.print("0. выход в главное меню");
        System.out.println();
    }

    public String getTitle() {
        return title;
    }

    public Menu(String title, ArrayList<MenuItem> items) {
        this.menuItems = items;
        this.title = title;
    }
}
