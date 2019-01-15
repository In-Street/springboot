import jodd.template.StringTemplateParser;
import jodd.util.Wildcard;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
    }

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
        int matchOne = Wildcard.matchOne(str, "A", "B","你*","*喜");
        System.out.println(matchOne);

    }


}
