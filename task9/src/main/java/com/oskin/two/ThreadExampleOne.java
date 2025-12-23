package com.oskin.two;

public class ThreadExampleOne extends Thread{
    @Override
    public void run(){
        ThreadExampleTwo threadExampleTwo = new ThreadExampleTwo();
        threadExampleTwo.start();
        try{
            threadExampleTwo.join();
        } catch (InterruptedException e){

        }
        System.out.println("Моя имя: "+this.getName());
    }
}
