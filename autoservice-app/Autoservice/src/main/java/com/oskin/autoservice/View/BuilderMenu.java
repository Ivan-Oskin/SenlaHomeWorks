package com.oskin.autoservice.View;

import com.oskin.Annotations.Singleton;


import java.util.ArrayList;

@Singleton
public class BuilderMenu {
    private ArrayList<MenuItem> items = new ArrayList<>();
    private String title = "";

    public void setTitle(String name) {
        title = name;
    }

    public void addItem(int number, String name, IAction action) {
        MenuItem item = new MenuItem(number, name, action);
        items.add(item);
    }

    public Menu build() {
        String newTitle = new String(this.title);
        ArrayList<MenuItem> newItems = new ArrayList<>(this.items);
        Menu menu = new Menu(newTitle, newItems);
        this.title = "";
        this.items.clear();
        return menu;
    }
}
