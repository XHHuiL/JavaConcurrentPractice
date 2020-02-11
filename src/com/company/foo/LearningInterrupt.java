package com.company.foo;

import java.util.Scanner;

public class LearningInterrupt {

    private static class MyRunnable implements Runnable {

        private static final int SLEEP_MILLISECONDS = 5000;

        @Override
        public void run() {
            while (true) {
                try{
                    System.out.println("ping");
                    Thread.sleep(SLEEP_MILLISECONDS);
                }catch (InterruptedException e){
                    System.out.println("Interrupted Exception");
                    break;
                }
            }
        }
    }

    private static class RunnableWithInterrupted extends Thread {

        @Override
        public void run() {
            // 如果线程中没有调用类似于sleep()这类可能抛出InterruptedException的函数，调用interrupt()会设置线程的中断标记
            // 可以继承Thread类，使用interrupted()函数来判断是否存在中断标记，存在返回true，不存在返回false，通过这种方式也可以提前结束线程
            while (!interrupted()){
                // do something
            }
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new MyRunnable());
        thread.setDaemon(true);
        thread.start();

        while (true){
            Scanner in = new Scanner(System.in);
            String line = in.nextLine();
            if (line.equals("break") || line.equals("exit") || line.equals("quit"))
                return;
            if (line.equals("interrupt"))
                // 调用interrupt()可以中断一个线程
                thread.interrupt();
        }
    }

}
