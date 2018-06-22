package cyf.gradle.interview.service.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 允许一个多个线程进行等待，直到其他线程执行完之后再执行
 * @author Cheng Yufei
 * @create 2018-06-22 16:56
 **/
public class CountDownLatchDemo {

    private static CountDownLatch countDownLatch = new CountDownLatch(7);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 7; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println("第" + finalI + "个人已完成任务！");
                //完成之后-1
                countDownLatch.countDown();
            }, "Thread_" + i).start();
        }

        countDownLatch.await();
        System.out.println("7人已全部完成任务，出发");
    }
}
/**
 *
 * 第1个人已完成任务！
 * 第4个人已完成任务！
 * 第3个人已完成任务！
 * 第2个人已完成任务！
 * 第5个人已完成任务！
 * 第6个人已完成任务！
 * 第7个人已完成任务！
 * 7人已全部完成任务，出发
 */

/**
 * 不使用计数器：
 * 第1个人已完成任务！
 * 第5个人已完成任务！
 * 7人已全部完成任务，出发
 * 第4个人已完成任务！
 * 第3个人已完成任务！
 * 第2个人已完成任务！
 * 第6个人已完成任务！
 * 第7个人已完成任务！
 */

