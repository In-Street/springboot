package cyf.gradle.interview.service.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Cheng Yufei
 * @create 2018-06-22 14:35
 **/
public class ReentrantReadWritDemo {

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    public void readHandle() {

        try {
            readWriteLock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + "获取读锁" + System.currentTimeMillis());
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //不释放锁其他线程无法获取到对象锁
            readWriteLock.readLock().unlock();
        }
    }

    public void writHandle() {

        try {
            readWriteLock.writeLock().lock();
            System.out.println(Thread.currentThread().getName() + "获取写锁" + System.currentTimeMillis());
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ReentrantReadWritDemo demo = new ReentrantReadWritDemo();
        /**
         * 俩个线程可同时获取到读锁,读读不互斥
         * Thread_1获取读锁1529650232759
         * Thread_2获取读锁1529650232759
         */
        new Thread(()->{
            demo.readHandle();
        },"Thread_1").start();

        new Thread(()->{
            demo.readHandle();
        },"Thread_2").start();

        /**
         * 写写互斥：获取锁的时间有差异
         * Thread_3获取写锁1529650317599
         * Thread_4获取写锁1529650320613
         */
        new Thread(()->{
            demo.writHandle();
        },"Thread_3").start();

        new Thread(()->{
            demo.writHandle();
        },"Thread_4").start();

        /**
         * 读写、写读互斥：
         * Thread_5获取读锁1529650846263
         * Thread_6获取写锁1529650851264
         */
        new Thread(()->{
            demo.readHandle();
        },"Thread_5").start();

        Thread.sleep(1000);
        new Thread(()->{
            demo.writHandle();
        },"Thread_6").start();
    }


}
