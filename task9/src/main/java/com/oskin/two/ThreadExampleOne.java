package com.oskin.two;

public class ThreadExampleOne extends Thread{
    private volatile boolean flag = true;
    private Byte i = 1;
    @Override
    public void run() {
        while (flag){
            synchronized (i){
                while (TwoThreads.getRunOneThread()){
                    try{
                        i.wait();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("Моя имя: "+this.getName());
                TwoThreads.setRunOneThread(true);
                i.notifyAll();
            }
        }
    }
    public void setFlag(boolean flag){
        this.flag = flag;
    }
}
