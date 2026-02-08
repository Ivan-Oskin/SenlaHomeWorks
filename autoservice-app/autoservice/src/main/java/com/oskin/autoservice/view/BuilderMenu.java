package com.oskin.autoservice.view;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BuilderMenu {
    private final  ArrayList<MenuItem> items = new ArrayList<>();
    private String title = "";

    public void setTitle(String name) {
        title = name;
    }

    public void addItem(int number, String name, IAction action) {
        MenuItem item = new MenuItem(number, name, action);
        items.add(item);
    }

    public Menu build() {
        String newTitle = this.title;
        ArrayList<MenuItem> newItems = new ArrayList<>(this.items);
        Menu menu = new Menu(newTitle, newItems);
        this.title = "";
        this.items.clear();
        return menu;
    }
}
