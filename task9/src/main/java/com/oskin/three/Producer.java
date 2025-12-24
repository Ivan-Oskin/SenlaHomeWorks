    package com.oskin.three;
    import java.util.Queue;
    import java.util.Random;

    public class Producer extends Thread {
        Queue<Integer> queue;
        private volatile boolean flag = true;
        Random random = new Random();
        @Override
        public void run(){
            while (flag){
                try{
                    synchronized (queue){
                        while (queue.size() > 4){
                            queue.wait();
                        }
                        queue.notifyAll();
                        queue.add(random.nextInt());
                        System.out.println(queue);
                    }
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        public Producer(Queue<Integer> queue){
            this.queue = queue;
        }
        public void setFlag(boolean flag){
            this.flag = flag;
        }
    }
