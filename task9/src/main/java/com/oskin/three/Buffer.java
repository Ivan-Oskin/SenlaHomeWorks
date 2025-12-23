package com.oskin.three;
import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
    static public Queue<Integer> queue = new LinkedList<>();
    static Producer producer = new Producer(queue);
    static Consumer consumer = new Consumer(queue);
    public static void main(String[] args){
        producer.start();
        consumer.start();
        try {
            Thread.sleep(2);
            producer.setFlag(false);
            consumer.setFlag(false);
        } catch (InterruptedException e){

        }
    }
}
