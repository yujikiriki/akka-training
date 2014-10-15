package co.s4n.tp;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    private final BlockingQueue<Runnable> workerQueue;
 
    public ThreadPool(int N)
    {
        workerQueue = new LinkedBlockingQueue<>();
        Thread[] workerThreads = new Thread[N];
 
        //Start N Threads and keep them running
        for (int i = 0; i < N; i++) {
            workerThreads[i] = new Worker("Pool Thread " + i);
            workerThreads[i].start();
        }
    }
 
    public void addTask(Runnable r)
    {
        try {
            workerQueue.put(r);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
 
    private class Worker extends Thread
    {
 
        public Worker(String name)
        {
            super(name);
        }
 
        public void run()
        {
            while (true) {
                try {
                    Runnable r = workerQueue.take();
                    r.run();
                } catch (InterruptedException | RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
 
}