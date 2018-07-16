package cyf.gradle.interview.service.concurrent;

/**
 * 线程基本操作方法
 *
 * @author Cheng Yufei
 * @create 2018-07-14 下午4:56
 **/
public class ThreadBasicOperation {

    static class MyThread extends Thread {

        @Override
        public void run() {
            try {
                for (int i = 1; i <= 100; i++) {
                    System.out.println(i);
                    //自行监控中断状态
                    if (this.isInterrupted()) {
                        System.out.println("MyThread 已中断");
                        //抛出中断异常，后线程停止运行
                        throw new InterruptedException();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thread.interrupted():  判断当前线程是否被中断，并清除中断状态
     * eg:     System.out.println("main :" + Thread.interrupted());    - true
     *          System.out.println("main :" + Thread.currentThread().isInterrupted());     -false
     * Thread.currentThread().isInterrupted() ：判断当前线程的中断状态 ，但不会清除状态
     * eg:      System.out.println("main :" + Thread.currentThread().isInterrupted());    - true
     *            System.out.println("main :" + Thread.currentThread().isInterrupted());  - true
     */

    public static void main(String[] args) {
        /**
         * interrupt:
         *          1.若不是当前线程会校验权限：SecurityException
         *          2.若线程调用interrupt()，后 sleep、wait、join 方法处于阻塞状态，则会清空中断状态，抛出 InterruptedException
         *
         */
        /*Thread.currentThread().interrupt();
        System.out.println("main :" + Thread.interrupted());
        System.out.println("main :" + Thread.currentThread().isInterrupted());
        System.out.println("main :" + Thread.currentThread().isInterrupted());*/

       /* try {
            Thread.currentThread().interrupt();
            Thread.sleep(1000);
            System.out.println("main 中断状态 : " + Thread.currentThread().isInterrupted());
            System.out.println("执行结束");
        } catch (InterruptedException e) {
            System.out.println("main: InterruptedException，中断状态：" + Thread.currentThread().isInterrupted());
            e.printStackTrace();
        }finally {
            System.out.println("finally");
        }*/

        MyThread myThread = new MyThread();
        myThread.start();
        //中断不会使线程停止运行，可自行判断状态然后处理
        myThread.interrupt();
        System.out.println("myThread 中断状态 : " + myThread.isInterrupted());
    }
}
