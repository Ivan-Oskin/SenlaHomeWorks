package com.oskin.one;

public class ThreadExample extends Thread{
    private boolean flag = true;
    private volatile boolean running = true;
    private Object object = new Object();
    @Override
    public void run(){
        try{
            Thread.sleep(200);
            synchronized (object){
                while(flag){
                    object.wait();
                }
            }
            while (running){

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setF(boolean flag){
        this.flag=flag;
        synchronized(object) {
            this.flag=flag;
            object.notify(); // Будим поток
        }
    }

    public void Stop(){
        this.running = false;
    }
}
