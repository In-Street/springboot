package cyf.gradle.interview.service.concurrent;

/**
 * @author Cheng Yufei
 * @create 2018-07-16 下午9:38
 **/
public class ThreadDaemon {

    static class MyThread extends Thread {

        @Override
        public void run() {
            for (int i = 1; i <= 1000; i++) {
                System.out.println("守护线程: "+i);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("main线程开始执行");
        MyThread myThread = new MyThread();
        //设置为守护线程
        myThread.setDaemon(true);
        myThread.start();
        Thread.sleep(2);
        System.out.println("main线程休眠结束");
    }
}
