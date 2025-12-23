package com.oskin.three;
import java.util.Queue;

public class Consumer extends Thread {
    Queue<Integer> queue;
    private volatile boolean flag = true;
    @Override
    public void run(){
        while (flag){
            try{
                synchronized (queue){
                    while (queue.isEmpty()){
                        queue.wait();
                    }
                    queue.notifyAll();
                    queue.poll();
                    System.out.println(queue);
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
    public Consumer(Queue<Integer> queue){
        this.queue = queue;
    }
    public void setFlag(boolean flag){
        this.flag = flag;
    }
}
