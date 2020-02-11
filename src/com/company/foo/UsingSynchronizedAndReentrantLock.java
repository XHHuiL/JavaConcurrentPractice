package com.company.foo;

import java.util.concurrent.locks.ReentrantLock;

public class UsingSynchronizedAndReentrantLock {
    /*
    * synchronized是JVM提供的锁机制，用于对共享资源的互斥访问，使用synchronized时需要获取对象锁
    * 当synchronized修饰成员方法时，获取的是调用此方法实例的锁；当synchronized修饰静态方法时，获取的是整个类的锁
    * */
    private static class SynchronizedExample {

        // 不同实例上执行这段代码，不需要进行同步
        public synchronized void instanceLock(){
            for (int i = 0; i < 10; i++) {
                System.out.print(i + " ");
            }
        }

        // 不同实例上执行这段代码，需要进行同步
        public static synchronized void classLock(){
            for (int i = 0; i < 10; i++) {
                System.out.print(i + " ");
            }
        }

    }

    /*
    * ReentrantLock 是 JDK 实现的，除非要使用ReentrantLock的高级功能，否则优先考虑使用synchronized
    * （ReentrantLock是JDK5引入的新机制，不是所有的JDK版本都支持；synchronized编程简单；使用synchronized不必担心没有释放锁而导致的死锁问题）
    * ReentrantLock的高级功能如下：
    * 1. 等待可中断（当持有锁的线程长期不释放锁时，等待锁的线程可以选择放弃等待，处理其他事情）
    * 2. 公平锁，公平锁是指多个线程在等待同一个锁时，必须按照申请锁的时间顺序来依次获得锁。ReentrantLock默认情况下非公平的，但可以配置成公平的。
    * 3. 锁绑定多个条件，一个 ReentrantLock 可以同时绑定多个 Condition 对象。
    * */
    private static class ReentrantLockExample {

        private static ReentrantLock lock;

        static {
            lock = new ReentrantLock();
        }

        public static void func(){
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.print(i + " ");
                }
            }finally {
                // 确保释放锁，避免发生死锁
                lock.unlock();
            }
        }

    }


    public static void main(String[] args) throws InterruptedException {
        SynchronizedExample sync1 = new SynchronizedExample();
        SynchronizedExample sync2 = new SynchronizedExample();
        System.out.println("Instance Lock");
        // 这里使用函数引用，简化程序编写
        new Thread(sync1::instanceLock).start();
        new Thread(sync2::instanceLock).start();

        Thread.sleep(1000);
        System.out.println("\nClass Lock");
        new Thread(SynchronizedExample::classLock).start();
        new Thread(SynchronizedExample::classLock).start();

        Thread.sleep(1000);
        System.out.println("\nReentrantLock");
        new Thread(ReentrantLockExample::func).start();
        new Thread(ReentrantLockExample::func).start();
    }

}
