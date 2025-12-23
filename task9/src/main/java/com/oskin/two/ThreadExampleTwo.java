package com.oskin.two;

public class ThreadExampleTwo extends Thread{
    @Override
    public void run(){
        System.out.println("Моя имя: "+this.getName());
    }
}
