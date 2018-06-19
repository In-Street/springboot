package cyf.gradle.interview.service.concurrent;

/**
 * @author Cheng Yufei
 * @create 2018-06-19 20:30
 **/
public class SynChronizedService {

//    private int count = 3;
    private static int count = 3;

    private synchronized void handle() {
        count--;
        System.out.println(Thread.currentThread().getName() + "count:" + count);
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
//            synService.handle();
        }).start();

        new Thread(()->{
            new SynChronizedService().handle();
//            synService.handle();
        }).start();

        new Thread(()->{
            new SynChronizedService().handle();
//            synService.handle();
        }).start();

        new Thread(()->{
            new SynChronizedService().handle();
//            synService.handle();
        }).start();
    }
}
