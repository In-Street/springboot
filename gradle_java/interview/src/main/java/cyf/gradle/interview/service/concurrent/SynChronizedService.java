package cyf.gradle.interview.service.concurrent;

import org.springframework.stereotype.Service;

/**
 * @author Cheng Yufei
 * @create 2018-06-19 20:30
 **/
@Service
public class SynChronizedService {

        private volatile int count = 3;
    private static int countSyn = 3;

    /**
     * 保证共享变量的一致性间接保证可见性
     */
    public synchronized void handle() {
        countSyn--;
        System.out.println(Thread.currentThread().getName() + "count:" + countSyn);
    }

    /**
     * volatile 不能保证操作的原子性，只能保证多线程间的可见性(jmeter测试)
     */
    public  void hand() {
        count--;
        System.out.println(Thread.currentThread().getName() + "count:" + count);
    }
    //=============================================================== Synchronized 的父子类继承 ======================================================================

    static class SynSuper {
        public int sum = 3;

        public synchronized void handleSuper() {
            sum--;
            System.out.println(Thread.currentThread().getName() + "super - sum:" + sum);
        }
    }

    static class SynSub extends SynSuper {
        public synchronized void handleSub() {
            while (sum > 0) {
                System.out.println(Thread.currentThread().getName() + "sub - sum:" + sum);
                handleSuper();
            }
        }
    }


    public static void main(String[] args) {
        /**
         * 一个对象一个锁：
         * 5个线程如果共用一个 synService ，则对count的操作是正确的：2，1，0，-1 ，此时无需将count设置为 static；
         *
         * 5个线程内各自 new SynChronizedService(),获取的是 5个不同的锁，相互之间不影响，count结果：2，2，2，2 ， 此时如果将 count 设为static，则即使是5个不同的锁对count操作
         * 是同一个,结果为：2，1，0，-1
         */
        SynChronizedService synService = new SynChronizedService();

        new Thread(()->{
            new SynChronizedService().handle();
//            synService.handleV();
        }).start();

        new Thread(()->{
            new SynChronizedService().handle();
//            synService.handleV();
        }).start();

        new Thread(()->{
            new SynChronizedService().handle();
//            synService.handleV();
        }).start();

        new Thread(()->{
            new SynChronizedService().handle();
//            synService.handleV();
        }).start();

       //父子类锁继承
       /* SynSub synSub = new SynSub();
        new Thread(() -> {
            synSub.handleSub();
        }).start();*/

    }

}
