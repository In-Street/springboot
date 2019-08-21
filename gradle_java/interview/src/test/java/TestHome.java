import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import cyf.gradle.interview.service.base.abstractandinterface.SonClass;
import cyf.gradle.interview.service.base.innerclass.Robot;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author Cheng Yufei
 * @create 2019-01-06 10:18
 **/
public class TestHome {

    /**
     * 利用 LinkedHashMap 自定义删除策略，删除不常访问的元素。
     */

    @Test
    public void linkedHashMap() {
        LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<Integer, String>(16, 0.75f, true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
                return size() > 3;
            }
        };

        linkedHashMap.put(1, "Tay");
        linkedHashMap.put(2, "Lor");
        linkedHashMap.put(3, "Candice");


        System.out.println(linkedHashMap.get(1) + "-----" + linkedHashMap.get(3));

        linkedHashMap.put(4, "Ariana");

        linkedHashMap.forEach((k, v) -> {
            System.out.println(k + "---->" + v);
        });
    }

    @Test
    public void i() {
        int j = 0;
        int x = 0;
        for (int i = 0; i < 100; i++) {
            //0 j始终为0
//            j = j++;
            //99
            x = j++;
            // 100
//            j = ++j;
        }
        System.out.println(x);
    }

    @Test
    public void array() {
        /**
         * Arrays.asList 转换的结果是Arrays类的静态内部类ArrayList【List接口的实现类】
         * 返回类型:List<int[]>
         */
        int[] i = new int[]{1, 2, 3, 4};
        List ints = Arrays.asList(i);
        // size = 1
        System.out.println(ints.size());

        List list = Arrays.asList(1, 2, 3, 4);
        // size = 4
        System.out.println(list.size());


        /**
         * 集合的equals: 比较里面的元素, 返回true
         */
        ArrayList<String> strings = new ArrayList<>();
        strings.add("asd");

        Vector vector = new Vector();
        vector.add("asd");
        System.out.println(strings + "-----" + vector);
        System.out.println(strings.equals(vector));

        /**
         * 使用类型：byte short int char String
         */
        switch ('a') {
        }
    }

    @Test
    public void getMillis() {
        /**
         * 获取 1970-01-01T00:00:00Z 至今的毫秒数
         */
        System.out.println(System.currentTimeMillis());

        System.out.println(new Date().getTime());

        System.out.println(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTimeInMillis());

        /**
         * 获取 秒数
         */
        System.out.println(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond());

        LocalDateTime.from(new Date().toInstant());


    }

    /**
     * String : intern 位置不同结果不同
     */
    @Test
    public void string() {
        String s = new String("1");
        //将值放入常量池，返回引用后并没有进行赋值
        s.intern();

        //常量池引用赋值给s1
        String s1 = new String("1").intern();
        String s2 = "1";
        //false
        System.out.println(s == s1);
        //false
        System.out.println(s == s2);
        //true
        System.out.println(s1 == s2);

    }

    /**
     * 可变、不可变集合
     */
    @Test
    public void immutableList() {
        //可变集合
        ArrayList<String> strings = Lists.newArrayList("A", "B");
        strings.add("C");
        System.out.println(strings);

        //不可变集合，再次add时：java.lang.UnsupportedOperationException
        List<String> strings1 = Arrays.asList("D", "E");
//        strings1.add("F");
        System.out.println(strings1);

        //不可变集合
        ImmutableList<String> strings2 = ImmutableList.of("G", "H");
        System.out.println(strings2);

    }

    @Test
    public void innerClass() {
        /**
         * 在外部类之外创建静态内部类、普通内部类方式
         */
        Robot.InnerRobot innerRobot = new Robot.InnerRobot();

        Robot.RobotRepair robotRepair = new Robot().new RobotRepair();

        SonClass sonClass = new SonClass();

    }

    /**
     * CollectionUtils.collect 、Iterables.transform  对集合元素的转换操作
     */
    @Test
    public void lambda() {

        ArrayList<String> strings = Lists.newArrayList("1", "2", "3");
        ArrayList<String> strings2 = Lists.newArrayList("aa", "bb", "cc");

        ArrayList<String> strings3 = Lists.newArrayListWithExpectedSize(3);
        CollectionUtils.collect(strings2, s -> ((String) s).toUpperCase(), strings3);
        System.out.println(strings3);

        Iterable<Integer> transform = Iterables.transform(strings, s -> Integer.valueOf(s) + 1);
        System.out.println(transform);

    }

    /**
     * 集合元素 运算
     */
    @Test
    public void intSummaryStatistics() {

        ArrayList<Integer> ints = Lists.newArrayList(1, 2, 3);

        IntSummaryStatistics statistics = ints.stream().mapToInt(i -> i).summaryStatistics();

        statistics.getAverage();
        statistics.getSum();
        statistics.getMax();
        statistics.getMin();


    }

    @Test
    public void buffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.get();

    }

    @Test
    public void completableFuture() {

        /*CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            return "a";
        });


        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            return "b";
        });

        CompletableFuture<String> f3 = f1.thenCombine(f2, (a, b) -> {
            return (a + b).toUpperCase();
        });

        System.out.println(f3.join());*/


////////////////////////////////////////////////////////////////////////////////////////////////////

        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            return "b";
        });

        CompletableFuture<String> f3 = f1.thenCombine(f2, (__, x) -> {
            return x.toUpperCase();
        });

        System.out.println(f3.join());


        CompletableFuture<Integer> exceptionally = CompletableFuture.supplyAsync(() -> {
            return 7 / 0;
        }).thenApply(r -> r * 10).exceptionally(e -> 0);

        System.out.println(exceptionally.join());

    }

    @Test
    public void classloader() {
        ClassLoader c1 = IO.class.getClassLoader();
        System.out.println(c1);

        ClassLoader c1Parent = c1.getParent();
        System.out.println(c1Parent);

        ClassLoader parent = c1Parent.getParent();
        System.out.println(parent);

    }

    @Test
    public void listiterator() {

        /**
         * ListIterator 从前向后、从后向前遍历，有add \set \ remove 操作
         */
        ArrayList<Integer> ints = Lists.newArrayList(1, 2, 3);
        ListIterator<Integer> listIterator = ints.listIterator();
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }

        while (listIterator.hasPrevious()) {
            System.out.println(listIterator.previous());
        }

        /**
         * Iterator 只能从前向后遍历，只有remove 操作
         */
        Iterator<Integer> iterator = ints.iterator();

        {
            String s = "abc";
            StringBuilder stringBuilder = new StringBuilder(s);
            stringBuilder.reverse();
            System.out.println(stringBuilder.toString());
        }
    }

}

// service tool windows ； tab ; 选中输入一个括号或者引号