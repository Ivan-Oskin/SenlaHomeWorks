package com.oskin.autoservice.view;

public class MenuItem {
    private final int number;
    private final String title;
    private final IAction action;

    public MenuItem(int number, String title, IAction action) {
        this.number = number;
        this.action = action;
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void execute() {
        action.execute();
    }
}
