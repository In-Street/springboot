import net.openhft.chronicle.map.ChronicleMap;
import org.junit.Test;

/**
 * k-v 存储
 * @author Cheng Yufei
 * @create 2019-03-04 11:39
 **/
public class ChronicleMapTest {

    private static ChronicleMap<CharSequence, CharSequence> map = ChronicleMap
            .of(CharSequence.class, CharSequence.class)
            .name("city-postal-codes-map")
            .averageKey("Amsterdam")
            .averageValueSize(500.0)
            .entries(50)
            .create();


    @Test
    public void instace() {
        map.put("1", "A");
        map.put("2", "B");
        map.put("3", "C");

        CharSequence sequence = map.get("3");
        System.out.println(sequence);
    }

    @Test
    public void get() {

    }


}
