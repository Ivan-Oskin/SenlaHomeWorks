package com.oskin.autoservice.View;

import java.util.ArrayList;

public class Menu {
    private String title;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    public void executeOfNumber(int number){
        if(number >= 0 && number < menuItems.size()){
            menuItems.get(number).execute();
        }
        else {
            System.out.print("Команда не найдена");
        }
    }
    public void getInformation(){
        System.out.println(title);
        for(int i = 0; i < menuItems.size(); i++){
            System.out.print((i+1) + ". " + menuItems.get(i).getTitle()+" ");
            if(i == 5) System.out.print("\n");
        }
        System.out.print("0. выход в главное меню");
        System.out.println();
    }

    public String getTitle(){
        return title;
    }
    public void addMenuItem(MenuItem menuItem){
        menuItems.add(menuItem);
    }
    public void execute(int number){
        boolean flag = false;
        for (MenuItem menuItem : menuItems){
            if(menuItem.getNumber() == number){
                menuItem.execute();
                flag = true;
            }
        }
        if(!flag) System.out.print("команда не найдена");
    }
    public Menu(String title, ArrayList<MenuItem> items){
        this.menuItems = items;
        this.title = title;
    }
}
