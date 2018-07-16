package cyf.gradle.interview.service.concurrent;

/**
 * @author Cheng Yufei
 * @create 2018-07-16 下午9:38
 **/
public class ThreadJoin {

    static class MyThread extends Thread {

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                System.out.println("MyThread: "+i);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("main线程开始执行");
        MyThread myThread = new MyThread();
        myThread.start();
        /**
         *  main线程调用myThread join() ，main线程阻塞，等待myThread 线程执行完后，main线程继续执行。
         */
        myThread.join();
        System.out.println("main线程继续执行");
        for (int i = 1; i <= 10; i++) {
            System.out.println("main: "+i);
        }
    }
}
