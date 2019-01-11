import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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

        System.out.println( LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTimeInMillis());

        /**
         * 获取 秒数
         */
        System.out.println( LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond());

        LocalDateTime.from(new Date().toInstant());


    }

}
