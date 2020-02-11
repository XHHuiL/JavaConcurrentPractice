package com.company.foo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UsingExecutor {
    /*
     * 主要有三种Executor
     * CachedThreadPool：一个任务创建一个线程
     * FixedThreadPool：所有任务只使用固定大小的线程
     * SingleThreadExecutor：相当于大小为1的FixedThreadPool
     * */
    private static int sum = 0;

    private static class MyRunnable implements Runnable {
        int value;

        public MyRunnable(int value) {
            this.value = value;
        }

        @Override
        public void run() {
            synchronized (MyRunnable.class) {
                sum += value;
            }
        }
    }

    private static void usingExecutor() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // ExecutorService executorService = Executors.newFixedThreadPool(6);
        // ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            executorService.execute(new MyRunnable(i));
        }
        // ExecutorService等待所有的线程执行完毕
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception");
        }
        System.out.printf("Sum: %d\n", sum);
    }

    public static void main(String[] args) {
        usingExecutor();
    }

}
