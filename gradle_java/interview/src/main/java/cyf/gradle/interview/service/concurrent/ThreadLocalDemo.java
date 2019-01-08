package cyf.gradle.interview.service.concurrent;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2018-06-20 16:07
 **/
public class ThreadLocalDemo {

    private static ThreadLocal<List<String>> threadLocal = new ThreadLocal<>();

    public void setThreadLocal(List<String> list) {
        threadLocal.set(list);
    }

    public void getThreadLocal() {
        List<String> strings = threadLocal.get();
        System.out.println(Thread.currentThread().getName() + "-------" + strings);
    }

    /**
     * 各个线程的ThreadLocal 独立不影响
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        ThreadLocalDemo demo = new ThreadLocalDemo();
        new Thread(() -> {

            List<String> list = Lists.newArrayList("A", "B", "C");
            demo.setThreadLocal(list);
            demo.getThreadLocal();
        },"Thread_A").start();

        Thread.sleep(1000);
        new Thread(() -> {
            List<String> list = Lists.newArrayList("D", "E");
            demo.setThreadLocal(list);
            demo.getThreadLocal();
        },"Thread_B").start();
    }

}
