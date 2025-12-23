package com.oskin.two;

public class TwoThreads {
    private volatile static boolean runOneThread = true;
    public static void main(String[] args) {
        ThreadExampleOne thread1 = new ThreadExampleOne();
        ThreadExampleTwo thread2 = new ThreadExampleTwo();
        thread1.start();
        thread2.start();
        try {
            Thread.sleep(4);
            thread1.setFlag(false);
            thread2.setFlag(false);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public static void setRunOneThread(boolean set){
        runOneThread = set;
    }
    public static boolean getRunOneThread(){
        return runOneThread;
    }

}
