package com.oskin.one;

public class AllThreadState {
    public static ThreadExample threadExample = new ThreadExample();
    public static void main(String[] args){
        Integer i = 0;
        System.out.println(threadExample.getState());
        threadExample.start();
        try{
            Thread.sleep(100);
            System.out.println(threadExample.getState());
            Thread.sleep(200);
            System.out.println(threadExample.getState());
            threadExample.setF(false);
            System.out.println(threadExample.getState());
            Thread.sleep(100);
            System.out.println(threadExample.getState());
            threadExample.Stop();
            Thread.sleep(200);
            System.out.println(threadExample.getState());
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
