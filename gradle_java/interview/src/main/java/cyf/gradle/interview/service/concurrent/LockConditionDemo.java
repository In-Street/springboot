package cyf.gradle.interview.service.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Cheng Yufei
 * @create 2018-06-21 16:31
 **/
public class LockConditionDemo {

    private ReentrantLock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void awaitHandle(Condition condition) {
        if (lock.tryLock()) {
            System.out.println(Thread.currentThread().getName() + "await - begin");
            try {
//                condition.await();
                //最多等待3秒来等待唤醒获取锁，否则结束等待
                condition.await(3000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName() + "await - end");

        }
    }

    public void signalHandle(Condition condition) {
        if (lock.tryLock()) {
            condition.signal();
            System.out.println("通知发出");
            lock.unlock();
        }
    }

    public void read() throws InterruptedException {
        if (lock.tryLock()) {
            System.out.println(Thread.currentThread().getName() + "开始读read");
            Thread.sleep(3000);
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockConditionDemo demo = new LockConditionDemo();
        Thread.sleep(500);
        new Thread(() -> {
            demo.awaitHandle(demo.conditionA);
        }, "Thread_1_conditionA").start();
        Thread.sleep(500);
        new Thread(() -> {
            demo.awaitHandle(demo.conditionB);
        }, "Thread_2_conditionB").start();
        Thread.sleep(500);
        new Thread(() -> {
            demo.awaitHandle(demo.conditionC);
        }, "Thread_3_conditionC").start();


        //condition可指定某个具体的condition获取锁，而不是参与锁竞争时获取的不确定性
        Thread.sleep(500);
        new Thread(() -> {
            demo.signalHandle(demo.conditionC);
        }, "Thread_signal_conditionC").start();

        Thread.sleep(500);
        new Thread(() -> {
            demo.signalHandle(demo.conditionB);
        }, "Thread_signal_conditionB").start();

        //没有唤醒conditionA，等待3秒后自动结束
        /*Thread.sleep(500);
        new Thread(() -> {
            demo.signalHandle(demo.conditionA);
        }, "Thread_signal_conditionA").start();*/


        //俩个线程同时要获取锁进行读操作时，只有一个线程获取到锁，能进行操作，另一个线程无法获取到锁，和synchronized相似只能有一个线程获取锁进行操作；为了提高效率读读操作可以同时进行，不需要互斥，引入ReentrantReadWritLock
        new Thread(() -> {
            try {
                demo.read();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Thread_1").start();

        new Thread(() -> {
            try {
                demo.read();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Thread_2").start();

    }


}
