package cyf.gradle.interview.service.concurrent;

import cyf.gradle.interview.modle.User;

import java.lang.ref.WeakReference;
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
       /* Map<Integer, String> map = Maps.newHashMap();
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
        }).start();*/


        System.out.println("====================================== 引用 ======================================");

       //强引用关联：user <-> new User
        User user = new User(1,"Taylor");
        //弱引用关联：ref <-> new User
        WeakReference<User> ref = new WeakReference<User>(user);
        System.out.println(ref.get());

        /**
         * 如果不置为null，调用gc，ref.get()还是可以获取到对象，说明没有回收；因为user与newUser之间还有强关联
         * 因为gc 工作时，无论当前内存是否足够，都会回收 *只被弱引用关联* 的对象，user = null,取消强引用关联，只有 ref <-> new User ，gc工作回收 new User;
         */
        user = null;
        System.gc();
        System.runFinalization();

        System.out.println(ref.get());


        /**         回收时间        生存时间
         * 强引用    从不回收          JVM 停止运行时
         * 软引用    内存不足时         内存不足时终止
         * 弱引用    gc工作时          gc 运行后终止
         * 虚引用
         */


    }

}
