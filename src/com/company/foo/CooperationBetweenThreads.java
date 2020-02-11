package com.company.foo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 进程间的协作
public class CooperationBetweenThreads {
    /*
     * 使用join()将一个线程暂时挂起，等待另一个线程执行完之后再继续执行
     * */

    private static void usingJoin() {
        A a = new A();
        B<A> b = new B<>(a);
        a.start();
        b.start();
    }

    private static class A extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println("A");
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
            }
        }
    }

    private static class B<T extends Thread> extends Thread {
        private T t;

        B(T t) {
            this.t = t;
        }

        @Override
        public void run() {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
            }
            System.out.println("B");
        }
    }

    /*
     * wait()、notify()、notifyAll()均属于Object的方法，需要与synchronized结合使用
     * 调用wait()使线程挂起，线程释放锁，其它线程调用notify()或notifyAll()来唤醒挂起的线程。
     * notify()随机唤醒一个挂起的线程
     * notifyAll()唤醒所有挂起的线程，但是被唤醒的线程会再次去竞争对象锁，只有一个线程能拿到锁，其他没有拿到锁的线程会被阻塞
     * wait()与sleep()的区别：wait()是Object的非静态方法，sleep()是Thread的静态方法；wait()会释放锁，sleep()不会释放锁
     * */
    private static void usingWaitAndNotifyAll() {
        WaitNotifyAllExample example = new WaitNotifyAllExample();
        new Thread(example::after).start();
        new Thread(example::before).start();
    }

    private static class WaitNotifyAllExample {

        public synchronized void before() {
            System.out.println("Before");
            notifyAll();
        }

        public synchronized void after() {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
            }
            System.out.println("After");
        }

    }

    /*
     * 在Condition对象上调用await()方法使线程等待，其它线程调用signal()或signalAll()方法唤醒等待的线程，Condition实例可以通过Lock实例获得
     * 相比于wait()，await()可以指定等待的条件（Condition），因此更加灵活
     * */
    private static void usingAwaitSignalAll() {
        AwaitSignalAllExample example = new AwaitSignalAllExample();
        new Thread(example::after).start();
        new Thread(example::before).start();
    }

    private static class AwaitSignalAllExample {

        private Lock lock;
        private Condition condition;

        {
            lock = new ReentrantLock();
            condition = lock.newCondition();
        }

        public void before() {
            lock.lock();
            try {
                System.out.println("Before With Condition");
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void after() {
            lock.lock();
            try {
                condition.await();
                System.out.println("After With Condition");
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
            } finally {
                lock.unlock();
            }
        }
    }


    public static void main(String[] args) {
        usingJoin();
        usingWaitAndNotifyAll();
        usingAwaitSignalAll();
    }

}
