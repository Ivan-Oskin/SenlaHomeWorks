package com.oskin.four;
public class MainGetTime {
    private static GetTime getTime = new GetTime(1);

    public static void main(String[] args){
        getTime.start();
        try{
            Thread.sleep(5000);
            getTime.Stop();
            getTime.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
