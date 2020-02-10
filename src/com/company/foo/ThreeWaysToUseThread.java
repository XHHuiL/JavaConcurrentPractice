package com.company.foo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreeWaysToUseThread {

    // First Way: implements Callable interface
    private static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            System.out.println("Call MyCallable");
            return "MyCallable";
        }
    }

    // Second Way: implements Runnable interface
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Run MyRunnable");
        }
    }

    // Third Way: extends Thread class
    private static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Run MyThread");
            super.run();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCallable myCallable = new MyCallable();
        FutureTask<String> task = new FutureTask<>(myCallable);
        Thread thread = new Thread(task);
        thread.start();

        thread = new Thread(new MyRunnable());
        thread.start();

        MyThread myThread = new MyThread();
        myThread.start();
    }
}
