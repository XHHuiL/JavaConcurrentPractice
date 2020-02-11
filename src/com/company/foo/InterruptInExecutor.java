package com.company.foo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InterruptInExecutor {
    /*
     * Executor有两种中断线程的方式
     * Method 1：调用shutDownNow()，相当于调用每个线程的interrupt()方法
     * Method 2：调用submit()方法来提交一个线程，然后调用返回Future对象的cancel()方法
     * */

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Runnable runnable = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
            }
        };
        // Method 1
        // executor.execute(runnable);
        //executor.shutdownNow();

        // Method 2
        Future<?> future = executor.submit(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
            }
        });
        // 当创建了Future实例，任务可能有以下三种状态
        // 等待状态。此时调用cancel()方法不管传入true还是false都会标记为取消，任务依然保存在任务队列中，但当轮到此任务运行时会直接跳过。
        // 完成状态。此时cancel()不会起任何作用，因为任务已经完成了。
        // 运行中。此时cancel()传入true会中断正在执行的任务，传入false则不会中断。
        // 所以此处调用Thread.sleep(1000)，先让main线程睡眠1s，让其他线程运行起来使之处于运行中
        Thread.sleep(1000);
        future.cancel(true);
        executor.shutdown();

        System.out.println("Main Run");
    }

}
