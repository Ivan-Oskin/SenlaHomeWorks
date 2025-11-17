package com.oskin.task6.View;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Navigator {
    private Scanner scanner = new Scanner(System.in);
    private Menu currentMenu;
    private ArrayList<Menu> menu = new ArrayList<>();

    public void addMenu(Menu menu){
        this.menu.add(menu);
    }

    public void printMenu(){
        this.currentMenu.getInformation();
    }

    public void getNamesOfMenu(){
        int i = 1;
        for(Menu menu : menu){
            System.out.print(i+ ". "+menu.getTitle()+ " ");
            i+=1;
        }
        System.out.print("0. выход");
        System.out.println();
    }

    public void navigate(int index){
        if(index > 0 && index <= menu.size()){
            this.currentMenu = menu.get(index-1);
            int input;
            printMenu();
            input = CarRepairInput.getInstance().inputInt();
            if(input == 0) return;
            else this.currentMenu.executeOfNumber(input-1);
        }
        else System.out.println("Такой команды нет");
    }

    public int GetCountMenu(){
        return menu.size();
    }

}
