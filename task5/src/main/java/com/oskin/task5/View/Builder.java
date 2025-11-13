package com.oskin.task5.View;
import java.util.ArrayList;

public class Builder {
    private ArrayList<MenuItem> items = new ArrayList<>();
    private String title = "";

    public void setTitle(String name){
        title = name;
    }
    public void addItem(int number, String name, IAction action){
        MenuItem item = new MenuItem(number, name, action);
        items.add(item);
    }
    public Menu build(){
        String newTitle = new String(this.title);
        ArrayList<MenuItem> newItems =new ArrayList<>(this.items);
        Menu menu = new Menu(newTitle, newItems);
        this.title = "";
        this.items.clear();
        return menu;
    }

}
