import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2019-01-06 10:18
 **/
public class TestHome {

    /**
     *  利用 LinkedHashMap 自定义删除策略，删除不常访问的元素。
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
}
