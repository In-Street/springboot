package cyf.gradle.interview.service.concurrent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2018-06-20 16:07
 **/
public class ThreadLocalAdvanceDemo {

    private static ThreadLocal<String> threadLocal_1 = new ThreadLocal<>();
    private static ThreadLocal<List<String>> threadLocal_2 = new ThreadLocal<>();
    private static ThreadLocal<Map<Integer,String>> threadLocal_3 = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        Map<Integer, String> map = Maps.newHashMap();
        map.put(100, "Candice");
        new Thread(() -> {
            threadLocal_1.set("100");
            threadLocal_2.set(Lists.newArrayList("Taylor","Swift"));
            threadLocal_3.set(map);
            System.out.println(threadLocal_2.get());
        }).start();

        new Thread(() -> {
            threadLocal_2.set(Lists.newArrayList("Candice","Cheng"));
            System.out.println(threadLocal_2.get());
        }).start();
    }

}
