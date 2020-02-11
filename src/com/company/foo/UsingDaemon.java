package com.company.foo;

import java.util.Scanner;

public class UsingDaemon {
    /*
    * Daemon 为守护进程，守护进程一般在后台运行提供必要的服务或定期执行任务，
    * 当所有的非守护进程结束时，程序终止，同时也会杀死所有的守护进程，
    * main()函数所在的进程属于非守护进程
    * */

    private static class MyRunnable implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("ping");
                    // sleep方法会休眠档当前正在执行的线程，参数millisec的单位为毫秒
                    // sleep方法可能会产生InterruptedException
                    // 但是异常不能跨线程传到main()函数中，所以必须在本地进行异常处理
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted Exception");
                    // yield方法调用时，表示当前线程已经完成了生命周期中最重要的部分，
                    // 建议线程调度器将资源分配给其他线程，只是建议，而且仅对同优先级的线程有用
                    Thread.yield();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        // 将thread设置为守护进程
        Thread thread = new Thread(new MyRunnable());
        thread.setDaemon(true);
        thread.start();

        while (true) {
            Scanner in = new Scanner(System.in);
            String line = in.nextLine();
            if (line.equals("break") || line.equals("exit") || line.equals("quit"))
                break;
            System.out.println(line);
        }
    }
}
