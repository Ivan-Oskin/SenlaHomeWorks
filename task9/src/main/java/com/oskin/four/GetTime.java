package com.oskin.four;

import java.time.LocalTime;

public class GetTime extends Thread {
    private int n;
    private boolean running = true;
    public GetTime(int n){
        this.n=n;
    }
    public void Stop(){
        this.running=false;
    }

    @Override
    public void run(){
        try{
            while (running){
                System.out.println(LocalTime.now());
                Thread.sleep(n*1000L);
            }
        } catch (InterruptedException e){
            System.out.println("Произошла ошибка с sleep");
        }
    }
}
