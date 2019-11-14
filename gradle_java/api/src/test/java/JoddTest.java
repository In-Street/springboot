import com.google.common.collect.Lists;
import jodd.buffer.FastCharBuffer;
import jodd.http.HttpRequest;
import jodd.template.StringTemplateParser;
import jodd.util.Wildcard;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Cheng Yufei
 * @create 2019-01-14 17:42
 **/
public class JoddTest {

    /**
     * 字符串模板
     */
    @Test
    public void string() {
        String temple = "Hello ${foo}. Today is ${dayName}.";

        Map<String, String> map = new HashMap<String, String>(2);
        map.put("foo", "Jodd");
        map.put("dayName", "mondy");

        StringTemplateParser parser = new StringTemplateParser();
        String parse = parser.parse(temple, str -> map.get(str));
        System.out.println(parse);

        // --------------------------- Example ------------------------------------
        List<String> wishText = Lists.newArrayList("在你附近，有一个${brotherOrSister}向你发送了心动消息，快去看看${heOrShe}和你说什么了吧~",
                "有人对你的约会表达了深深的诚意，快来看看${heOrShe}是谁吧~", "${nickname}对你的约会留言了，快来回复一下，不要让${heOrShe}等太久哦~");

        int random = ThreadLocalRandom.current().nextInt(wishText.size());
        String content = wishText.get(random);

        HashMap<String, String> maps = new HashMap<>(4);
        maps.put("brotherOrSister", "小哥哥");
        maps.put("heOrShe", "他");
        maps.put("nickname", "九天");

        String result = parser.parse(content, s -> maps.get(s));
        System.out.println(result);

    }

    /**
     * 匹配
     */
    @Test
    public void wildcard() {

        String str = "你喜欢，喜";
        boolean equalsOrMatch = Wildcard.equalsOrMatch(str, "?喜*");
        //true
        System.out.println(equalsOrMatch);

        boolean match = Wildcard.match(str, "?喜*");
        System.out.println(match);

        /**
         * 返回第一个匹配成功的数组下标
         */
        int matchOne = Wildcard.matchOne(str, "A", "B", "你*", "*喜");
        System.out.println(matchOne);

        String path = "/data/logs";
        boolean matchPath = Wildcard.matchPath(path, "/*/logs");
        System.out.println("matchPath: - " + matchPath);

    }

    /**
     * 拼接
     */
    @Test
    public void buffer() {
        FastCharBuffer buffer = new FastCharBuffer(2);
        String s = buffer.append("A").append("B").toString();
        System.out.println(s);
    }

}
