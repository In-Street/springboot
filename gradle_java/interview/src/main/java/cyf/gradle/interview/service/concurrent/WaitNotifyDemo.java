package cyf.gradle.interview.service.concurrent;

import com.google.common.collect.Lists;
import com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Cheng Yufei
 * @create 2018-06-20 17:47
 **/
public class WaitNotifyDemo {

    private static LinkedList list = Lists.newLinkedList();
    private static Object lock = new Object();
    private static int max = 4;
    private static int min = 0;

    public WaitNotifyDemo() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        System.out.println("初始化集合完成");
    }


    public void put(String value) {
        synchronized (lock) {
            if (Objects.equals(list.size(), max)) {
                try {
                    System.out.println("ThreadA - wait begin:" + FastDateFormat.getInstance("HH:mm:ss").format(new Date(System.currentTimeMillis())));
                    lock.wait();
                    System.out.println("ThreadA - wait end:" + FastDateFormat.getInstance("HH:mm:ss").format(new Date(System.currentTimeMillis())));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.add(value);
            System.out.println("添加元素:" + value);
            lock.notify();
        }
    }

    public void get() {
        synchronized (lock) {
            if (Objects.equals(list.size(), min)) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Object pop = list.pop();
            System.out.println("消费元素:" + pop);
            lock.notify();
        }

    }

    static class ThreadA {
        private Object lock;

        public ThreadA(Object lock) {
            this.lock = lock;
        }

        public void aHandle() {
            synchronized (lock) {
                if (!Objects.equals(list.size(), 10)) {
                    try {
                        System.out.println("ThreadA - wait begin:" + FastDateFormat.getInstance("HH:mm:ss").format(new Date(System.currentTimeMillis())));
                        lock.wait();
                        System.out.println("ThreadA - wait end:" + FastDateFormat.getInstance("HH:mm:ss").format(new Date(System.currentTimeMillis())));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class ThreadB {
        private Object lock;

        public ThreadB(Object lock) {
            this.lock = lock;
        }

        public void bHandle() {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {

                    list.add(String.valueOf(i));
                    if (Objects.equals(list.size(), 8)) {
                        System.out.println("notify通知发送");
                        lock.notify();
                    }
                    System.out.println("元素添加：" + String.valueOf(i));
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {

        /**
         * 结果：
         * 初始化集合完成
         * ThreadA - wait begin:20:03:16
         * 消费元素:A
         * ThreadA - wait end:20:03:17
         * 添加元素:E
         * ThreadA - wait begin:20:03:17
         * 消费元素:B
         * ThreadA - wait end:20:03:18
         * 添加元素:F
         */
        WaitNotifyDemo demo = new WaitNotifyDemo();
        new Thread(() -> {
            demo.put("E");
            demo.put("F");
        }).start();

        Thread.sleep(1000);

        new Thread(() -> {
            demo.get();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            demo.get();
        }).start();

        System.out.println("===================================ThreadA - ThreadB ==================================================");

        /**
         * 结果：
         * 初始化集合完成
         * ===================================ThreadA - ThreadB ==================================================
         * ThreadA - wait begin:20:24:33
         * 元素添加：0
         * 元素添加：1
         * 元素添加：2
         * notify通知发送
         * 元素添加：3
         * 元素添加：4
         * 元素添加：5
         * 元素添加：6
         * 元素添加：7
         * 元素添加：8
         * 元素添加：9
         * ThreadA - wait end:20:24:34
         */
        new Thread(() -> {
            ThreadA threadA = new ThreadA(demo);
            threadA.aHandle();
        }).start();

        Thread.sleep(1000);

        new Thread(() -> {
            ThreadB threadB = new ThreadB(demo);
            threadB.bHandle();
        }).start();

    }
}
