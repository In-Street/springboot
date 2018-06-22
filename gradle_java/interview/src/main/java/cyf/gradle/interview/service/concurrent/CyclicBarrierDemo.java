package cyf.gradle.interview.service.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Cheng Yufei
 * @create 2018-06-22 19:34
 **/
public class CyclicBarrierDemo {

    /**
     * CyclicBarrier：多线程并发控制工具，让一组线程到达屏障时阻塞，直到最后一个线程到达，屏障开启，所有被拦截的线程才能继续干活。
     * @param args
     */
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, new Runnable() {
            @Override
            public void run() {
                //全部到齐后，进行后续任务操作
                System.out.println(Thread.currentThread().getName());
                System.out.println("7位法师已全部到来");
                handle();
            }
        });
        //等待全部法师到来
        for (int i = 1; i <= 7; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println("第" + finalI + "位法师已到来");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "Thread_" + i).start();
        }
    }

    public static void handle() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                //全部完成后召唤
                System.out.println("召唤");
            }
        });
        //等待全部法师完成任务,法师到来 await 通知屏障已到，阻塞等待最后一个法师到来
        for (int i = 1; i <= 7; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println("第" + finalI + "位法师完成任务");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "Thread_" + i).start();

        }
    }
}
