package com.oskin.autoservice.View;

public class MenuItem {
    private int number;
    private String title;
    private IAction action;

    public MenuItem(int number, String title, IAction action){
        this.number = number;
        this.action = action;
        this.title = title;
    }

    public int getNumber(){
        return number;
    }
    public String getTitle(){
        return title;
    }
    public void execute(){
        action.execute();
    }
}
